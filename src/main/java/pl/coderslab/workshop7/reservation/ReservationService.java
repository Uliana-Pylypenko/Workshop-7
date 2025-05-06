package pl.coderslab.workshop7.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation create(Long userId, Long accommodationId, LocalDate reservationStart, LocalDate reservationEnd);


}
