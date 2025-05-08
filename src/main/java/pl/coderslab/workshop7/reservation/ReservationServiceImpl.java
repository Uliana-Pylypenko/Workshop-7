package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Log4j2
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private AccommodationRepository accommodationRepository;
    private Clock clock;

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private void checkUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
    }

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
            throw new IllegalArgumentException("Reservation start date is after reservation end date");
        }

        reservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        reservation.setDateOfReservation(LocalDateTime.now(clock));

        Reservation savedReservation = reservationRepository.save(reservation);

        logger.info("Saved reservation: {}", savedReservation);

        return savedReservation;
    }

    @Override
    public List<Reservation> findAllByUserId(Long userId) {
        checkUserExists(userId);
        return reservationRepository.findAllByUserId(userId);
    }

    @Override
    public List<Reservation> findPastReservationsByUserId(Long userId) {
        checkUserExists(userId);
        return reservationRepository.findPastReservationsByUserId(userId, LocalDate.now(clock));
    }

    @Override
    public List<Reservation> findCurrentReservationsByUserId(Long userId) {
        checkUserExists(userId);
        return reservationRepository.findCurrentReservationsByUserId(userId, LocalDate.now(clock));
    }

    @Override
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        reservation.setReservationStatus(status);
        reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public ReservationConfirmation generateConfirmation(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            ReservationConfirmation confirmation = new ReservationConfirmation();
            Reservation reservation = reservationOptional.get();
            confirmation.setReservationId(id);
            confirmation.setDateOfReservation(reservation.getDateOfReservation());
            confirmation.setReservationStart(reservation.getReservationStart());
            confirmation.setReservationEnd(reservation.getReservationEnd());
            confirmation.setReservationStatus(reservation.getReservationStatus());

            User user = reservation.getUser();
            confirmation.setUsername(user.getUsername());
            confirmation.setEmail(user.getEmail());

            Accommodation accommodation = reservation.getAccommodation();
            confirmation.setAccommodationName(accommodation.getName());
            long days = ChronoUnit.DAYS.between(reservation.getReservationStart(), reservation.getReservationEnd()) + 1L;
            confirmation.setTotalPrice(days * accommodation.getPricePerDay());
            confirmation.setLocation(accommodation.getLocation());

            return confirmation;
        } else {
            throw new EntityNotFoundException("Reservation not found");
        }
    }

}
