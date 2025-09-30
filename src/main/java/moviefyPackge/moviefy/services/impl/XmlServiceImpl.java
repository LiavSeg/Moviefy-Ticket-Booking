package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import moviefyPackge.moviefy.domain.dto.ReportsDto.BookingReports.BookingItemReport;
import moviefyPackge.moviefy.domain.dto.ReportsDto.BookingReports.BookingReportDto;
import moviefyPackge.moviefy.domain.dto.ReportsDto.DateRangeDto;
import moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.PopularMovieItemDto;
import moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.PopularMovieRepDto;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.*;
import moviefyPackge.moviefy.enums.Xml.XmlImportType;
import moviefyPackge.moviefy.enums.Xml.XmlOpType;
import moviefyPackge.moviefy.enums.Xml.XmlStatus;
import moviefyPackge.moviefy.mappers.BookingMapper;
import moviefyPackge.moviefy.repositories.BookingRepository;
import moviefyPackge.moviefy.repositories.SeatAllocationRepository;
import moviefyPackge.moviefy.repositories.XmlReportRepository;
import moviefyPackge.moviefy.services.Util.XmlUtil;
import moviefyPackge.moviefy.services.XmlService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class XmlServiceImpl implements XmlService {
    @Autowired
    private final XmlReportRepository  xmlReportRepository;
    @Autowired
    private final MovieServiceImpl movieService;
    @Autowired
    private final ShowtimeServiceImpl showtimeService;
    @Autowired
    private final XmlUtil xmlUtil;
    @Autowired
    private final ResourceLoader resourceLoader;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private SeatAllocationRepository seatAllocationRepository;
    private final SeatAllocationServiceImpl seatAllocationService;
    private final Logger logger;

    private static final Map<String, String> XML_FILES = Map.of(
            "movies", "XML/Movies_Import_Files/movies_importt.xml",
            "showtimes", "XML/Showtime_Import_Files/showtimes_import.xml",
            "BOOKINGS_REPORT","bookings_report.xsd",
            "SHOWTIMES_REPORT","showtimes_reports.xsd"
    );
    private static final Map<String, String> XSD_FILES = Map.of(

            "BOOKINGS_REPORT","XSD/ReportStructureValidation/bookings_report.xsd",
            "SHOWTIMES_REPORT","XSD/EntitiesImportValidation/showtimes_reports.xsd",
            "POPULAR_MOVIE_REPORT","XSD/ReportStructureValidation/PopularMovies.xsd"
    );

    private static final Map<String, String> XSL_FILES = Map.of(
            "movies", "XSL/movies.xsl",
            "showtimes", "XSL/showtimes.xsl",
            "BOOKINGS_REPORT", "XSL/booking_reports.xsl",
            "SHOWTIMES_REPORT", "XSL/showtimes_report.xsl",
            "POPULAR_MOVIE_REPORT","XSL/popular-movies.xsl"

    );


    public XmlServiceImpl(MovieServiceImpl movieService, XmlReportRepository xmlReportRepository,
                          ShowtimeServiceImpl showtimeService, XmlUtil xmlUtil, ResourceLoader resourceLoader, SeatAllocationServiceImpl seatAllocationService) {
        this.movieService = movieService;
        this.xmlReportRepository = xmlReportRepository;
        this.showtimeService = showtimeService;
        this.xmlUtil = xmlUtil;
        this.resourceLoader = resourceLoader;
        this.seatAllocationService = seatAllocationService;
        this.logger = LoggerFactory.getLogger(XmlServiceImpl.class);
    }

    @Override
        public String exportBookingReport(XmlRequestDto reportRequestDto, String format) {
        Class<?>[] classes = { BookingReportDto.class, BookingItemReport.class };
        try {
            String bookingType = reportRequestDto.getOpType();
            BookingReportDto reportDto = compileDataForReportBookings(reportRequestDto);
            String report = xmlUtil.marshal(reportDto,classes);
            xmlUtil.validateXmlString(report, XSD_FILES.get(bookingType));
            saveReportSuccessfulReportEntity(reportRequestDto.getOpType(),report);
            return  format.equalsIgnoreCase("html")?
                    xmlUtil.transformXmlToHtml(report, XSL_FILES.get(bookingType)):report;
        }
        catch (Exception e){
            logger.warn("Could not generate {} report in format {}",reportRequestDto.getOpType(),format);
            throw new RuntimeException(e.getMessage());
        }
    }

    private BookingReportDto compileDataForReportBookings(XmlRequestDto reportRequestDto){
        List<BookingEntity> bookings =bookingRepository.
                findBookingEntitiesByBookingTimeBetween(reportRequestDto.getStartDate(),
                        reportRequestDto.getEndDate());

        List<BookingItemReport> bookingItems = bookings.stream().map(bookingMapper::fromBookingEntityToItem).toList();
        DateRangeDto dateRangeDto = DateRangeDto.builder()
                .from(reportRequestDto.getStartDate())
                .to(reportRequestDto.getEndDate())
                .build();

        BookingReportDto bookingReportDto = BookingReportDto.builder()
                .total(bookingItems.size())
                .bookings(bookingItems)
                .build();
        bookingReportDto.setGeneratedAt(LocalDateTime.now());
        bookingReportDto.setRange(dateRangeDto);
        return bookingReportDto;
    }

    public String exportPopularMoviesReport(XmlRequestDto reportRequestDto,String format){
        Class<?>[] classes = { PopularMovieRepDto.class, PopularMovieItemDto.class };
        Object[] dataObjects = getDataForPopularMovie(reportRequestDto.getStartDate(),reportRequestDto.getEndDate());
        DateRangeDto dateRangeDto = (DateRangeDto) dataObjects[0];
        List<PopularMovieItemDto> top =
                (dataObjects[1] instanceof List)
                        ? (List<PopularMovieItemDto>) dataObjects[1]
                        : Collections.emptyList();
        PopularMovieRepDto report = PopularMovieRepDto.builder().moviesList(top).build();
        report.setRange(dateRangeDto);
        String reportXml = xmlUtil.marshal(report,classes);
        xmlUtil.validateXmlString(reportXml, XSD_FILES.get("POPULAR_MOVIE_REPORT"));
        saveReportSuccessfulReportEntity(reportRequestDto.getOpType(),reportXml);
        return format.equalsIgnoreCase("html")?
                xmlUtil.transformXmlToHtml(reportXml, XSL_FILES.get("POPULAR_MOVIE_REPORT")):reportXml;
    }


    @Override
    public String convertXmlToHtml(String type) {
        try {

            String xmlFileName = XML_FILES.get(type);
            String xslFileName = XSL_FILES.get(type);
            if (xslFileName == null || xmlFileName == null)
                throw new Exception("Preview type not supported: " + type);

            Resource xslResource = resourceLoader.getResource("classpath:" + xslFileName);
            Resource xmlResource = resourceLoader.getResource("classpath:" + xmlFileName);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslResource.getInputStream()));

            Source xml = new StreamSource(xmlResource.getInputStream());

            StringWriter writer = new StringWriter();
            StreamResult res = new StreamResult(writer);

            transformer.transform(xml, res);

            return writer.toString();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to convert XML to HTML. XML: " +  XML_FILES.get(type) + ", XSL: " + XSL_FILES.get(type), e);

        }
    }

    @Override
    public XmlEntity importFromXml(MultipartFile xmlFile,XmlImportType type) {

        String xdsFile  = type.getXdsFileName();
        xmlUtil.validateXmlWithXsd(xmlFile,xdsFile);
        Object wrapper = xmlUtil.unmarshal(xmlFile, type.getWrapperClass());
        String relativePath;
        if (wrapper instanceof ShowtimeXmlWrapper showtimes) importAndSaveShowtimeList(showtimes);
        else if  (wrapper instanceof MovieXmlWrapper movies) importAndSaveMovieList(movies);

        try { relativePath = xmlUtil.saveUploadedFile(xmlFile, type); }
        catch (IOException e) {
            logger.warn("Could not save importData");
            throw new RuntimeException(e.getMessage());
        }

        XmlEntity xmlEntity =  XmlEntity.builder()
                .fileName(xmlFile.getOriginalFilename())
                .opType(XmlOpType.IMPORT_DATA.format(type.name()))
                .storagePath(relativePath)
                .status(XmlStatus.SUCCESS)
                .build();

        return createXmlEntity(xmlEntity);

    }

    @Override
    public XmlEntity createXmlEntity(XmlEntity xmlEntity){return xmlReportRepository.save(xmlEntity);}



    private void importAndSaveShowtimeList(@NotNull ShowtimeXmlWrapper showtimes){
        List<ShowtimeXmlDto> shows = showtimes.getShowtimes();
        for (ShowtimeXmlDto xmlDto:shows){
            Optional<MovieEntity> movie = movieService.findById(xmlDto.getMovieId());
            if (movie.isEmpty()){
                logger.warn("Warning: Could not import Showtime - A movie with id {} does not exists",xmlDto.getMovieId());
                continue;
            }

            ShowtimeEntity showtime = ShowtimeEntity.builder()
                    .movie(movie.get())
                    .price(xmlDto.getPrice())
                    .theater(xmlDto.getTheater())
                    .startTime(xmlDto.getStartTime())
                    .endTime(xmlDto.getEndTime())
                    .build();

            try {
                logger.info(showtime.toString());
                ShowtimeEntity showtimeNew = showtimeService.createShowtime(showtime);
                seatAllocationService.createDefaultSeats(showtimeNew.getId());
            }
            catch (IllegalStateException stateException){
                logger.warn("Warning: {}", String.valueOf(stateException));
            }
        }
    }
    private void importAndSaveMovieList(@NotNull MovieXmlWrapper movieList){
        List<MovieXmlDto> movies = movieList.getMovies();
        for (MovieXmlDto xmlDto:movies){
            MovieEntity movie = MovieEntity.builder()
                    .title(xmlDto.getTitle())
                    .genre(xmlDto.getGenre())
                    .duration(xmlDto.getDuration())
                    .releaseDate(xmlDto.getReleaseDate())
                    .imageUrl(xmlDto.getImageUrl())
                    .description(xmlDto.getDescription())
                    .language(xmlDto.getLanguage())
                    .trailerUrl(xmlDto.getTrailerUrl())
                    .build();
            if (!movieService.existsInDb(movie))
                movieService.createMovie(movie);
            else
                logger.warn("Warning: {} Is already in DB - did not add again.", xmlDto.getTitle());


        }
    }


     private Object[] getDataForPopularMovie(LocalDateTime from,LocalDateTime to){
        Object[] objects = new Object[2];
        objects[0] = DateRangeDto.builder()
                 .from(from)
                 .to(to)
                 .build();
        Pageable top5 = PageRequest.of(0, 5);
        objects[1] = seatAllocationRepository.findTopMoviesByOccupiedSeatsAndBookings(from,to,top5);

        return objects;
     }

    private void saveReportSuccessfulReportEntity(String type, String xmlReport){

        try{
            XmlEntity audit = xmlUtil.saveReportInStorage(type,xmlReport);
            xmlReportRepository.save(audit);
        }
        catch (IOException e){
            XmlEntity failed = XmlEntity.builder()
                    .fileName(type+"_report"+LocalDateTime.now())
                    .opType(type)
                    .storagePath("FAILED_REPORT_GENERATION")
                    .status(XmlStatus.FAIL)
                    .build();
            xmlReportRepository.save(failed);
        }

    }
}


