package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
        LocalDate reservationStart = LocalDate.parse(reservationStartString);
        LocalDate reservationEnd = LocalDate.parse(reservationEndString);
        Reservation savedReservation = reservationService.create(userId, accommodationId, reservationStart, reservationEnd);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);

//        try {
//            LocalDate reservationStart = LocalDate.parse(reservationStartString);
//            LocalDate reservationEnd = LocalDate.parse(reservationEndString);
//            Reservation savedReservation = reservationService.create(userId, accommodationId, reservationStart, reservationEnd);
//            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
//
//        } catch (DateTimeParseException | IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        } catch (EntityNotFoundException e1) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

    }
}
