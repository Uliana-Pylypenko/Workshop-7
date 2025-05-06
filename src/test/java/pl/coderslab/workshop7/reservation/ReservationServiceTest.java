package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @InjectMocks
    private ReservationServiceImpl service;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Long userId;
    private Accommodation accommodation;
    private Long accommodationId;
    private Reservation reservation;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User();
        user.setId(userId);

        accommodationId = 1L;
        accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        reservationStart = LocalDate.of(2020, 1, 1);
        reservationEnd = LocalDate.of(2020, 2, 1);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setAccommodation(accommodation);
        reservation.setReservationStart(reservationStart);
        reservation.setReservationEnd(reservationEnd);
        reservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
    }

    @Test
    void shouldSaveReservation() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = service.create(userId, accommodationId, reservationStart, reservationEnd);

        assertThat(savedReservation.getId()).isEqualTo(reservation.getId());
        assertThat(savedReservation.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedReservation.getAccommodation().getId()).isEqualTo(accommodation.getId());
        assertThat(savedReservation.getReservationStart()).isEqualTo(reservationStart);
        assertThat(savedReservation.getReservationEnd()).isEqualTo(reservationEnd);
        assertThat(savedReservation.getReservationStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void shouldThrowExceptionWhenAccommodationNotFound() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("Accommodation not found");
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        reservationStart = LocalDate.of(2020, 2, 1);
        reservationEnd = LocalDate.of(2020, 1, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Reservation start date is after reservation end date");
    }

    @Test
    void findByUserIdTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserId(userId)).thenReturn(List.of(reservation));

        List<Reservation> foundReservations = service.findAllByUserId(userId);

        assertThat(foundReservations)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation);
    }

    @Test
    void findByUserIdShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});

        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

}