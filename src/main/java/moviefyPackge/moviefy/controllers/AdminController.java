package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.services.AdminService;
import moviefyPackge.moviefy.services.impl.SeatAllocationServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller that exposes administration endpoints.
 * Provides reporting and management features for admins such as:
 * - Booking statistics over a date range
 * - Income statistics over a date range
 * - Popular movie statistics over a date range
 * - Seat allocation for showtimes
 * Access to this controller is restricted to users with the ADMIN role.
 */
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
@RestController
public class AdminController {
    private final AdminService adminService;
    private final ErrorHandler errorHandler;
    private final SeatAllocationServiceImpl seatAllocationService;
    private final LoggerWrapper<AdminController> logger ;


    /**
     * Initializes the controller with required services.
     * @param adminService service for admin analytics and operations
     * @param seatAllocationService service responsible for seat allocation
     */
    public AdminController(AdminService adminService, SeatAllocationServiceImpl seatAllocationService){
        this.adminService = adminService;
        this.seatAllocationService = seatAllocationService;
        this.errorHandler = new ErrorHandler(AdminController.class);
        this.logger = new LoggerWrapper<>(AdminController.class,"Admin");
        logger.startLog();
    }

    /**
     * Returns booking statistics grouped by month within the given date range.
     * @param start start of range in ISO date-time format
     * @param end end of range in ISO date-time format
     * @return HTTP 200 with a list of BookingByMonthDto on success,
     *         or an error response on failure
     */
    @GetMapping("/stats/bookings-per-range")
    public ResponseEntity<?> bookingsPerMonthStats(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                   @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)  {
        try{
            logger.LogInfo("Booking stats within a specified range ");
            List<BookingByMonthDto> bookingStats = adminService.getBookingsPerMonth(start, end);
            logger.success();
            return new ResponseEntity<>(bookingStats, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.badRequest(" ",e,"/admin/stats/bookings-per-range");
        }
    }

    /**
     * Returns income statistics within the given date range.
     * @param start start of range in ISO date-time format
     * @param end end of range in ISO date-time format
     * @return HTTP 200 with a list of IncomeRangeDto on success, or an error response on failure
     */
    @GetMapping("/stats/income-per-range")
    public ResponseEntity<?> incomeRangeStats(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                   @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)  {
        try{
            logger.LogInfo("Income stats within a specified range");
            List<IncomeRangeDto> incomeByRange = adminService.getIncomeByRange(start, end);
            logger.success();
            return new ResponseEntity<>(incomeByRange, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.badRequest(" ",e,"/admin/stats/income-per-range");
        }
    }

    /**
     * Creates default seat allocations for the given showtime.
     * @param showtimeId identifier of the showtime
     * @return HTTP 200 with a confirmation message on success,
     *         or an error response on failure
     */
    @PostMapping("/create-default")
    public ResponseEntity<?> createDefaultSeats(@RequestParam Integer showtimeId) {
        try
        {
            logger.LogInfo("Seats assignment for a now showtime");
            seatAllocationService.createDefaultSeats(showtimeId);
            logger.success();
            return new ResponseEntity<>("Default seats created for showtimeId " + showtimeId, HttpStatus.OK);
        }
        catch(Exception e){
            return errorHandler.badRequest(" ",e,"/admin/create-default");
        }
    }

    /**
     * Returns the most popular movies by income within the given date range.
     * @param start start of range in ISO date-time format
     * @param end end of range in ISO date-time format
     * @return HTTP 200 with a list of popular movies on success,
     *         or an error response on failure
     */
    @GetMapping("/stats/popular-movie-range")
    public ResponseEntity<?>popularMoviesStats(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                              @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)  {
        try{
            logger.LogInfo("Popular movies within a range");
            List<?> popularMoviesListDto = adminService.getHighestIncomeMovieRange(start, end);
            logger.success();
            return new ResponseEntity<>(popularMoviesListDto, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.badRequest(" ",e,"/admin/stats/popular-movie-range");
        }
    }


}









