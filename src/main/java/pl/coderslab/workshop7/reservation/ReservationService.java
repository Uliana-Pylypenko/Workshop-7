package pl.coderslab.workshop7.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation create(Long userId, Long accommodationId, LocalDate reservationStart, LocalDate reservationEnd);

    List<Reservation> findAllByUserId(Long userId);

    List<Reservation> findPastReservationsByUserId(Long userId);

    List<Reservation> findCurrentReservationsByUserId(Long userId);

    Reservation updateReservationStatus(Long id, ReservationStatus status);

    ReservationConfirmation generateConfirmation(Long id);
}
