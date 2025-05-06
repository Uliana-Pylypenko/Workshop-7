package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
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


    @Test
    void shouldSaveReservation() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Long accommodationId = 1L;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setAccommodation(accommodation);
        reservation.setReservationStart(reservationStart);
        reservation.setReservationEnd(reservationEnd);
        reservation.setReservationStatus(ReservationStatus.IN_PROGRESS);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = service.create(userId, accommodationId, reservationStart, reservationEnd);

        assertThat(savedReservation.getId()).isEqualTo(reservation.getId());
        assertThat(savedReservation.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedReservation.getAccommodation().getId()).isEqualTo(accommodation.getId());
        assertThat(savedReservation.getReservationStart()).isEqualTo(reservationStart);
        assertThat(savedReservation.getReservationEnd()).isEqualTo(reservationEnd);
        //assertThat(savedReservation.getReservationStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void shouldThrowExceptionWhenAccommodationNotFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("Accommodation not found");
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Long accommodationId = 1L;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        LocalDate reservationStart = LocalDate.of(2020, 2, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 1, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Reservation start date is after reservation end date");
    }

}