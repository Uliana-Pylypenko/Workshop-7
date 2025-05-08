package pl.coderslab.workshop7.reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationConfirmation {
    private Long reservationId;
    private LocalDateTime dateOfReservation;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private ReservationStatus reservationStatus;

    private String username;
    private String email;

    private String accommodationName;
    private double totalPrice;
    private String location;
}
