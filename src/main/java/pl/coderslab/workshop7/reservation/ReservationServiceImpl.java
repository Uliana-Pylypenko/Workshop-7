package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private AccommodationRepository accommodationRepository;

    @Override
    public Reservation create(Long userId, Long accommodationId, LocalDate reservationStart, LocalDate reservationEnd) {
        Reservation reservation = new Reservation();

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            reservation.setUser(user.get());
        } else {
            throw new EntityNotFoundException("User not found");
        }

        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isPresent()) {
            reservation.setAccommodation(accommodation.get());
        } else {
            throw new EntityNotFoundException("Accommodation not found");
        }

        if (reservationStart.isBefore(reservationEnd)) {
            reservation.setReservationStart(reservationStart);
            reservation.setReservationEnd(reservationEnd);
        } else {
            throw new EntityNotFoundException("Reservation start date is after reservation end date");
        }

        return reservationRepository.save(reservation);

    }
}
