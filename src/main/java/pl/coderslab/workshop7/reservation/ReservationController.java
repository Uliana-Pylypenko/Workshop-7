package pl.coderslab.workshop7.reservation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestParam Long userId,
                                                         @RequestParam Long accommodationId,
                                                         @RequestParam String reservationStartString,
                                                         @RequestParam String reservationEndString) {

        try {
            LocalDate reservationStart = LocalDate.parse(reservationStartString);
            LocalDate reservationEnd = LocalDate.parse(reservationEndString);
            Reservation savedReservation = reservationService.create(userId, accommodationId, reservationStart, reservationEnd);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);

        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Reservation>> getAllReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.findAllByUserId(userId), HttpStatus.OK);
    }

    // no tests
    @GetMapping("/past/{userId}")
    public ResponseEntity<List<Reservation>> getPastReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.findPastReservationsByUserId(userId), HttpStatus.OK);
    }

    // no tests
    @GetMapping("/current/{userId}")
    public ResponseEntity<List<Reservation>> getCurrentReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.findCurrentReservationsByUserId(userId), HttpStatus.OK);
    }
}
