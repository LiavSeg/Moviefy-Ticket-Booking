package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingRequestDto;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.BookingMapper;
import moviefyPackge.moviefy.services.BookingService;
import moviefyPackge.moviefy.services.SeatAllocationService;
import moviefyPackge.moviefy.services.Util.EmailSenderHandler;
import moviefyPackge.moviefy.services.impl.PdfServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Exposes booking operations for creating bookings, listing user bookings,
 * fetching reserved seats for a showtime, and generating booking tickets as PDF.
 * Cross-origin requests are allowed from the configured frontend origin.
 * Method security is enabled at the application level.
 */
@Slf4j
@EnableMethodSecurity
@CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping("/bookings")
@RestController
public class BookingController {
    private final BookingService bookingService;
    private final ErrorHandler errorHandler;
    private final BookingMapper bookingMapper;
    private final PdfServiceImpl pdfServiceImpl;
    private final SeatAllocationService seatAllocationService;
    private final LoggerWrapper<BookingController> logger ;
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender mailSender;
    private final EmailSenderHandler emailSenderHandler;
    /**
     * Initializes the controller with required services.
     * @param bookingService business logic for bookings
     * @param bookingMapper mapper between booking DTOs and entities
     * @param pdfServiceImpl service for generating PDF tickets
     * @param seatAllocationService service for seat reservation and confirmation
     */
    public BookingController(BookingService bookingService, BookingMapper bookingMapper, PdfServiceImpl pdfServiceImpl, SeatAllocationService seatAllocationService, JavaMailSender mailSender, EmailSenderHandler emailSenderHandler) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
        this.mailSender = mailSender;
        this.emailSenderHandler = emailSenderHandler;
        this.errorHandler = new ErrorHandler(BookingController.class);
        this.pdfServiceImpl = pdfServiceImpl;
        this.seatAllocationService = seatAllocationService;
        this.logger = new LoggerWrapper<>(BookingController.class,"Booking");
        logger.startLog();

    }

    /**
     * Creates a new booking and confirms all selected seats.
     * @param bookingDto request payload containing user, showtime, and seat details
     * @return HTTP 200 with the created booking as a DTO on success,
     *         HTTP 404 if related resources are missing,
     *         HTTP 409 if seat conflict occurs,
     *         HTTP 400 for other validation or processing errors
     */
    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody BookingRequestDto bookingDto) {
        boolean err = false;
        BookingEntity b = null;
        try {
            logger.LogInfo("Creating a new booking");
            logger.fromEntity();
            BookingEntity currBooking = bookingMapper.fromBookingRequestDto(bookingDto);
            BookingEntity newBooking = bookingService.createBooking(currBooking);
            logger.LogInfo("Booking created - confirms seats");
            seatAllocationService.confirmSeats(newBooking.getShowtime().getId(),newBooking.getSeatList(),newBooking);
            logger.costume("All booking seats in SOLD status");
            logger.success();
            b = newBooking;
            return new ResponseEntity<>(bookingMapper.fromBookingEntity(newBooking), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            err =true;
            return errorHandler.notFound("", e, "/bookings");
        } catch (IllegalStateException e) {
            err =true;
            return errorHandler.conflict("", e, "/bookings");
        } catch (Exception e) {
            err =true;
            return errorHandler.badRequest("", e, "/bookings");
        }
        finally {
            if (!err){
                try{
                    assert b!=null;
                    byte[] pdf = pdfServiceImpl.generateTicketPdf(b);
                    emailSenderHandler.sendBookingConfirmationEmail(b.getUser().getEmail(),b,pdf);
                }
            catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    /**
     * Returns all reserved seats for a specific showtime.
     * @param showtimeId identifier of the showtime
     * @return HTTP 200 with a map containing the key "reservedSeats" mapped to a list of seat identifiers
     */
    @GetMapping(path ="/{showtimeId}/seats")
    public ResponseEntity<?> getBookingByMovieId(@PathVariable Integer showtimeId){
        List<String> seats = bookingService.getSeatsForBookingByMovieId(showtimeId);
        return new ResponseEntity<>(Map.of("reservedSeats",seats),HttpStatus.OK);
    }
    /**
     * Lists all bookings for a specific user.
     *
     * @param id user identifier as a UUID string
     * @return HTTP 200 with a list of BookingDto on success,
     *         HTTP 400 on invalid input or processing errors
     */
    @GetMapping(path = "/user/{id}")
    public ResponseEntity<?> userBookingsList(@PathVariable String id){
            try{
                logger.LogInfo("Getting user's "+id+ " all bookings");
                List<BookingDto> bookingDtos =bookingService.getAllUserBooking(UUID.fromString(id));
                logger.success();
                return new ResponseEntity<>(bookingDtos,HttpStatus.OK);
            }
            catch (Exception e){
                return errorHandler.badRequest(" ",e,"/bookings/user/{"+id+"}");
            }
    }
    /**
     * Generates and returns a booking ticket in PDF format.
     *
     * @param id booking identifier as UUID
     * @return HTTP 200 with PDF bytes and content headers on success,
     *         HTTP 404 if the booking does not exist,
     *         HTTP 400 if PDF generation fails
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> getBookingTicketPdf(@PathVariable UUID id) {
        try {
            logger.LogInfo("Generating user's "+id+ "Booking in PDF format");
            BookingEntity booking = bookingService.findBooking(id);
            byte[] pdfBytes = pdfServiceImpl.generateTicketPdf(booking);
            logger.LogInfo("Booking in PDF format was generated");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename("ticket.pdf").build());
            logger.success();
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return errorHandler.notFound("Booking not found", e, "/bookings/" + id + "/pdf");
        } catch (Exception e) {
            return errorHandler.badRequest("PDF generation failed", e, "/bookings/" + id + "/pdf");
        }
    }


    @GetMapping("/__mailtest")
    public ResponseEntity<?> test(@RequestParam String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress("moviefyservice@gmail.com", "Moviefy"));
            helper.setTo(to);
            helper.setSubject("Moviefy Email test");
            helper.setText("Damnnn it works", false);

            mailSender.send(mimeMessage);

            return ResponseEntity.ok(Map.of("status", "sent", "to", to));
        } catch (Exception e) {
            return errorHandler.badRequest("Mail send failed", e, "/bookings/__mailtest");
        }
    }


}
