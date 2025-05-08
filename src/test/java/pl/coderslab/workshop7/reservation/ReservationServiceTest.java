package pl.coderslab.workshop7.reservation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Mock
    private Clock clock;

    private User user;
    private Long userId;
    private Accommodation accommodation;
    private Long accommodationId;
    private Reservation reservation1;
    private Reservation reservation2;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private Clock fixedClock;


    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC"));
        LocalDate fixedDate = LocalDate.now(fixedClock);

        userId = 1L;
        user = new User(userId, "test", "test@gmail.com", "test");

        accommodationId = 1L;
        accommodation = new Accommodation();
        accommodation.setId(accommodationId);

        reservationStart = LocalDate.of(2020, 1, 1);
        reservationEnd = LocalDate.of(2020, 2, 1);

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setUser(user);
        reservation1.setAccommodation(accommodation);
        reservation1.setReservationStart(reservationStart);
        reservation1.setReservationEnd(reservationEnd);
        reservation1.setReservationStatus(ReservationStatus.IN_PROGRESS);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUser(user);
        reservation2.setAccommodation(accommodation);
        reservation2.setReservationStart(fixedDate.minusDays(1));
        reservation2.setReservationEnd(fixedDate.plusDays(1));
        reservation2.setReservationEnd(reservationEnd);
        reservation2.setReservationStatus(ReservationStatus.IN_PROGRESS);
    }

    @Test
    void whenSaveReservation_thenReturnSavedReservation() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation1);

        Reservation savedReservation = service.create(userId, accommodationId, reservationStart, reservationEnd);

        assertThat(savedReservation.getId()).isEqualTo(reservation1.getId());
        assertThat(savedReservation.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedReservation.getAccommodation().getId()).isEqualTo(accommodation.getId());
        assertThat(savedReservation.getReservationStart()).isEqualTo(reservationStart);
        assertThat(savedReservation.getReservationEnd()).isEqualTo(reservationEnd);
        assertThat(savedReservation.getReservationStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
    }

    @Test
    void whenSaveReservationAndUserNotFound_thenThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void whenSaveReservationAndAccommodationNotFound_thenThrowEntityNotFoundException() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.create(userId, accommodationId, reservationStart, reservationEnd);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("Accommodation not found");
    }

    @Test
    void whenSaveReservationAndStartDateAfterEndDate_thenThrowIllegalArgumentException() {
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
    void whenFindAllByUserId_thenReturnListOfReservations() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserId(userId)).thenReturn(List.of(reservation1, reservation2));

        List<Reservation> foundReservations = service.findAllByUserId(userId);

        assertThat(foundReservations)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(reservation1, reservation2);
    }

    @Test
    void whenFindAllByUserIdAndUserNotFound_thenThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                {service.findPastReservationsByUserId(userId);});

        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void whenFindPastReservationsByUserId_thenReturnPastReservations() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(reservationRepository.findPastReservationsByUserId(userId, LocalDate.now(clock))).thenReturn(List.of(reservation1));

        List<Reservation> pastReservations = service.findPastReservationsByUserId(userId);

        assertThat(pastReservations)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation1)
                .doesNotContain(reservation2);
    }

    @Test
    void whenFindPastReservationsByUserIdAndUserNotFound_thenThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                        {service.findPastReservationsByUserId(userId);});
        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void whenFindCurrentReservationsByUserId_thenReturnListOfReservations() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(reservationRepository.findCurrentReservationsByUserId(userId, LocalDate.now(clock))).thenReturn(List.of(reservation2));

        List<Reservation> currentReservations = service.findCurrentReservationsByUserId(userId);

        assertThat(currentReservations)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation2)
                .doesNotContain(reservation1);
    }

    @Test
    void whenFindCurrentReservationsByUserIdAndUserNotFound_thenThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                        {service.findCurrentReservationsByUserId(userId);});

        assertThat(entityNotFoundException.getMessage()).isEqualTo("User not found");
    }

    @Test
    void whenUpdateReservationStatus_thenReturnReservationWithUpdatedStatus() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));

        ReservationStatus newStatus = ReservationStatus.CONFIRMED;
        Reservation updatedReservation = service.updateReservationStatus(1L, newStatus);

        assertThat(updatedReservation.getReservationStatus()).isEqualTo(newStatus);

        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void whenUpdateNonExistentReservationStatus_thenThrowEntityNotFoundException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateReservationStatus(1L, ReservationStatus.CONFIRMED));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }


}