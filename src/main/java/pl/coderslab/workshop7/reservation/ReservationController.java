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

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations(@RequestParam Long userId) {
        return new ResponseEntity<>(reservationService.findAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/past")
    public ResponseEntity<List<Reservation>> getPastReservations(@RequestParam Long userId) {
        return new ResponseEntity<>(reservationService.findPastReservationsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<List<Reservation>> getCurrentReservations(@RequestParam Long userId) {
        return new ResponseEntity<>(reservationService.findCurrentReservationsByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<Reservation> updateReservationStatus(@RequestParam Long id, @RequestParam ReservationStatus status) {
        return new ResponseEntity<>(reservationService.updateReservationStatus(id, status), HttpStatus.OK);
    }

    @GetMapping("/confirmation")
    public ResponseEntity<ReservationConfirmation> getReservationConfirmation(@RequestParam Long id) {
        return new ResponseEntity<>(reservationService.generateConfirmation(id), HttpStatus.OK);
    }
}
