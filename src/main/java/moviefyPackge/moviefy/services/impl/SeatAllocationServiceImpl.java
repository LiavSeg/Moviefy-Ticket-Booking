package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.Entities.SeatAllocation;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.SeatAvailabilityDto;
import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.SeatAvailabilityResponseDto;
import moviefyPackge.moviefy.enums.SeatState;
import moviefyPackge.moviefy.repositories.SeatAllocationRepository;
import moviefyPackge.moviefy.repositories.ShowtimeRepository;
import moviefyPackge.moviefy.services.SeatAllocationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatAllocationServiceImpl implements SeatAllocationService {

        private static final Duration HOLD_DURATION = Duration.ofMinutes(5);
        private final SeatAllocationRepository repo;
        private final ShowtimeRepository showtimeRepo;

    public SeatAllocationServiceImpl(SeatAllocationRepository repo, ShowtimeRepository showtimeRepo) {
        this.repo = repo;
        this.showtimeRepo = showtimeRepo;
    }

        @Transactional
        public void holdSeats(Integer showtimeId, List<String> seatLabels) {
            List<String> sorted = new ArrayList<>(seatLabels);
            Collections.sort(sorted);
            ensureRowsExist(showtimeId, sorted);
            List<SeatAllocation> seats = repo.lockByShowtimeAndLabels(showtimeId, sorted);
            LocalDateTime now = LocalDateTime.now();

            for (SeatAllocation s : seats) {
                boolean heldAndValid = s.getState() == SeatState.RESERVED && s.getExpiresAt() != null && s.getExpiresAt().isAfter(now);
                if (s.getState() == SeatState.SOLD || heldAndValid) {
                    throw new IllegalStateException("Seat unavailable: " + s.getSeatLabel());
                }
                s.setState(SeatState.RESERVED);
                s.setExpiresAt(now.plus(HOLD_DURATION));
                s.setBooking(null);
            }
            repo.saveAll(seats);
        }

        @Transactional(readOnly = true)
        public SeatAvailabilityResponseDto getAvailability(Integer showtimeId) {
            LocalDateTime now = LocalDateTime.now();
            List<SeatAllocation> list = repo.findByShowtime_Id(showtimeId);

            List<SeatAvailabilityDto> items = list.stream().map(s -> {

                SeatState eff = (s.getState() == SeatState.RESERVED && s.getExpiresAt() != null && s.getExpiresAt().isBefore(now))
                        ? SeatState.AVAILABLE : s.getState();
                return new SeatAvailabilityDto(s.getSeatLabel(), eff, s.getExpiresAt());
            }).toList();

            return new SeatAvailabilityResponseDto(showtimeId, items);
        }

        @Transactional
        public void confirmSeats(Integer showtimeId, List<String> seatLabels, BookingEntity booking) {
            List<String> sorted = new ArrayList<>(seatLabels);
            Collections.sort(sorted);

            List<SeatAllocation> seats = repo.lockByShowtimeAndLabels(showtimeId, sorted);
            LocalDateTime now = LocalDateTime.now();

            for (SeatAllocation s : seats) {
                if (s.getState() != SeatState.RESERVED || s.getExpiresAt() == null || !s.getExpiresAt().isAfter(now)) {
                    throw new IllegalStateException("Seat not held or expired: " + s.getSeatLabel());
                }
                s.setState(SeatState.SOLD);
                s.setExpiresAt(null);
                s.setBooking(booking);
            }
            repo.saveAll(seats);
        }

        @Transactional
        @Scheduled(fixedRate = 60_000)
        public void cleanupExpiredHolds() {
            repo.releaseExpiredHolds();
        }

        private void ensureRowsExist(Integer showtimeId, List<String> seatLabels) {
            ShowtimeEntity st = showtimeRepo.findById(showtimeId)
                    .orElseThrow(() -> new IllegalArgumentException("Showtime not found"));

            Map<String, SeatAllocation> existing = repo.findByShowtime_Id(showtimeId)
                    .stream().collect(Collectors.toMap(SeatAllocation::getSeatLabel, x -> x));

            List<SeatAllocation> toCreate = new ArrayList<>();
            for (String label : seatLabels) {
                if (!existing.containsKey(label)) {
                    SeatAllocation s = new SeatAllocation();
                    s.setShowtime(st);
                    s.setSeatLabel(label);
                    s.setState(SeatState.AVAILABLE);
                    s.setExpiresAt(null);
                    s.setBooking(null);
                    toCreate.add(s);
                }
            }
            if (!toCreate.isEmpty()) repo.saveAll(toCreate);
        }


    @Transactional
    public void createDefaultSeats(Integer showtimeId) {
        ShowtimeEntity st = showtimeRepo.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found"));

        List<SeatAllocation> seats = new ArrayList<>();
        String[] rowLabels = {"A", "B", "C", "D", "E"};
        int cols = 8;

        for (String row : rowLabels) {
            for (int col = 1; col <= cols; col++) {
                SeatAllocation seat = new SeatAllocation();
                seat.setShowtime(st);
                seat.setSeatLabel(row + "-" + col);
                seat.setState(SeatState.AVAILABLE);
                seat.setExpiresAt(null);
                seat.setBooking(null);
                seats.add(seat);
            }
        }
        repo.saveAll(seats);
    }
    }



