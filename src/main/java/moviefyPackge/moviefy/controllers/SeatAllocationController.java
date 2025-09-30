package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.ReserveRequestDto;
import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.SeatAvailabilityDto;
import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.SeatAvailabilityResponseDto;
import moviefyPackge.moviefy.services.impl.SeatAllocationServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Exposes seat allocation endpoints for checking availability,
 * holding seats temporarily, and confirming seat reservations.
 * All endpoints operate on a given showtime ID and a list of seat labels.
 */

@RestController
@RequestMapping("/seats")
public class SeatAllocationController {

    private final LoggerWrapper<SeatAllocationController> logger = new LoggerWrapper<>(SeatAllocationController.class,"Seat Allocation");
    private final SeatAllocationServiceImpl seatService;



    /**
     * Initializes the controller with the required seat allocation service.
     * @param seatService implementation of the seat allocation logic
     */
    public SeatAllocationController(SeatAllocationServiceImpl seatService) {
        this.seatService = seatService;
        logger.startLog();

    }
    /**
     * Returns seat availability for a given showtime.
     * @param showtimeId identifier of the showtime
     * @return list of SeatAvailabilityDto representing available and reserved seats,
     *         or null if an error occurs
     */
    @GetMapping("/availability")
    public List<SeatAvailabilityDto> getAvailability(@RequestParam Integer showtimeId) {
        try {
            logger.LogInfo(" Getting seats availabillity for showtime "+showtimeId);
            SeatAvailabilityResponseDto responseDto = seatService.getAvailability(showtimeId);
            if (responseDto.getSeats().size()==40)
                logger.costume("MAKE WARNING");

            return seatService.getAvailability(showtimeId).getSeats();//TODO CHANGE RETURN TYPE
        }
        catch (Exception e){return null;}
    }


    /**
     * Holds a list of seats for a specific showtime for a short period (e.g., 5 minutes).
     * @param req object containing showtimeId and seat labels to be reserved
     * @return HTTP 200 on success, or null if an error occurs
     */
    @PostMapping("/hold")
    public ResponseEntity<?> holdSeats(@Valid @RequestBody ReserveRequestDto req) {
        try {
            logger.LogInfo(" Reserving seats " + req.getSeatLabels() + " for " + req.getShowtimeId() + ", 5 min period");
            seatService.holdSeats(req.getShowtimeId(), req.getSeatLabels());
            logger.success();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
           return null;
        }
    }


    /**
     * Confirms and marks the specified seats as sold for the given showtime.
     * @param req object containing showtimeId and seat labels to be confirmed
     * @return HTTP 200 on success, or null if an error occurs
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmSeats(@RequestBody ReserveRequestDto req) {
        try {
            logger.LogInfo(" Updating seats " + req.getSeatLabels() + " of " + req.getShowtimeId() + "to SOLD status");
            seatService.confirmSeats(req.getShowtimeId(), req.getSeatLabels(), null);
            logger.success();
                return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return null;
        }
    }

}
