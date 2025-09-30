package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.XmlRequestDto;
import moviefyPackge.moviefy.enums.Xml.XmlImportType;
import moviefyPackge.moviefy.enums.Xml.XmlOpType;
import moviefyPackge.moviefy.enums.Xml.XmlStatus;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.impl.XmlMapperImpl;
import moviefyPackge.moviefy.services.impl.XmlServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.MissingResourceException;
/**
 * Exposes administrative endpoints for importing XML data, exporting reports,
 * and generating HTML previews from stored XML files.
 * Only accessible by users with ADMIN role.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/xml")
@PreAuthorize("hasRole('ADMIN')")
public class XmlController {

    private final XmlServiceImpl xmlService;
    private final ErrorHandler errorHandler;
    private final XmlMapperImpl xmlMapper;
    private final LoggerWrapper<XmlController> logger = new LoggerWrapper<>(XmlController.class,"XML Handler");

    /**
     * Initializes the controller with required dependencies.
     * @param xmlService business logic for XML import/export
     * @param xmlMapper mapper for converting between XmlEntity and DTO
     */
    public XmlController(XmlServiceImpl xmlService, XmlMapperImpl xmlMapper) {
        this.xmlService = xmlService;
        this.xmlMapper = xmlMapper;
        this.errorHandler = new ErrorHandler(XmlController.class);
        logger.LogInfo("XML module endpoint is set and ready to operate.");
    }


    /**
     * Imports a new XML file, validates its contents, and persists metadata.
     * @param file uploaded XML file
     * @param type the data type being imported
     * @return HTTP 200 with XmlEntity DTO on success,
     *         or HTTP 400 with a failed operation stored for audit
     */
    @PostMapping("/import")
    public ResponseEntity<?> importXml(@RequestParam MultipartFile file, @RequestParam XmlImportType type) {

        try {
            logger.LogInfo("XML "+type+" import request initiated by admin.");
            logger.LogInfo("Parsing and validating XML data");
            XmlEntity xmlEntity = xmlService.importFromXml(file, type);
            logger.LogInfo("XML operation was documented in XML Chart.");
            logger.success();
            return new ResponseEntity<>(xmlMapper.mapTo(xmlEntity), HttpStatus.OK);
        } catch (Exception e) {
            XmlEntity failed = XmlEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .opType(XmlOpType.IMPORT_DATA.format(type.name()))
                    .status(XmlStatus.FAIL)
                    .storagePath("NOT STORED - FAILED")
                    .build();
            xmlService.createXmlEntity(failed);
            return errorHandler.badRequest("", e, "/xml/import");
        }
    }



    /**
     * Returns an HTML preview of an XML file from storage.
     * @param type name of the XML resource to preview
     * @return HTTP 200 with rendered HTML,
     *         or HTTP 400 if preview generation fails
     */
    @GetMapping(value = "/html/{type}/preview", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getXmlAsHtml(@PathVariable String type) throws MissingResourceException {
        try {
            logger.LogInfo("New xml to html conversion request - "+type);
            String preview = xmlService.convertXmlToHtml(type);
            logger.success();
            return new ResponseEntity<>(preview, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.badRequest(type, e, "/xml/html/" + type + "/preview");
        }
    }

    /**
     * Generates an XML report based on a date range and report type.
     * @param reportRequest the date range and report operation type
     * @param format format of the report: xml, html, or download
     * @return HTTP 200 with report content in requested format,
     *         or HTTP 400 on failure
     */
    @PostMapping("/export")
    public ResponseEntity<?> generateXmlReport(@RequestBody XmlRequestDto reportRequest,
                                               @RequestParam(defaultValue = "xml") String format) {
        try {

            String opType = reportRequest.getOpType();
            String from = reportRequest.getStartDate().toString();
            String to = reportRequest.getEndDate().toString();
            String report = getOpTypeReport(opType,reportRequest,format);
            logger.LogInfo("New "+opType+" XML report generation request in "+format+" format");
            return switch (format) {
                case "html" -> ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(report);
                case "download" -> {
                    String reportFileName = String.format("%s_from_%s_to_%s", opType, from, to);
                    yield ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_XML)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportFileName)
                            .body(report);
                }
                default -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_XML)
                        .body(report);
            };
        } catch (Exception e) {
            return errorHandler.badRequest("", e, "");//TODO FIX THIS AND ADD MORE ERROR HANDLING!
        }
    }
    /**
     * Returns the correct XML report content based on operation type.
     * @param opType report type (e.g., BOOKINGS_REPORT)
     * @param reportRequest request parameters (dates and type)
     * @param format response format: xml, html, or download
     * @return formatted XML report content
     */
    private String getOpTypeReport(String opType,XmlRequestDto reportRequest,String format){
        String report = "";
        if (opType.equalsIgnoreCase("BOOKINGS_REPORT"))
            report = xmlService.exportBookingReport(reportRequest,format);
        else if (opType.equalsIgnoreCase("POPULAR_MOVIE_REPORT"))
            report = (String)xmlService.exportPopularMoviesReport(reportRequest,format);
        logger.LogInfo("Report was generated successfully");
        return report;
    }

}

