package pl.coderslab.workshop7.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("select r from Reservation r where r.user.id = ?1 and r.reservationEnd < ?2")
    List<Reservation> findPastReservationsByUserId(Long userId, LocalDate currentDate);

    @Query("select r from Reservation r where r.user.id = ?1 and ((?2 between r.reservationStart and r.reservationEnd) or (r.reservationStart > ?2 ))")
    List<Reservation> findCurrentReservationsByUserId(Long userId, LocalDate currentDate);
}
