package pl.coderslab.workshop7.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("select r from Reservation r where r.user.id = ?1 and r.reservationEnd < local_date")
    List<Reservation> findPastReservationsByUserId(Long userId);

    @Query("select r from Reservation r where r.user.id = ?1 and local_date between r.reservationStart and r.reservationEnd")
    List<Reservation> findCurrentReservationsByUserId(Long userId);
}
