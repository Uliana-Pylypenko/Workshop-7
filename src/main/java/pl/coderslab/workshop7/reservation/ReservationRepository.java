package pl.coderslab.workshop7.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("select r from Reservation r where r.user.id = ?1 and r.reservationEnd < ?1 ")
    List<Reservation> findPastReservationsByUserId(Long userId, LocalDate currentDate);

    @Query("select r from Reservation r where r.user.id = ?1 and ((current_date between r.reservationStart and r.reservationEnd) or (r.reservationStart > current_date )) ")
    List<Reservation> findCurrentReservationsByUserId(Long userId);
}
