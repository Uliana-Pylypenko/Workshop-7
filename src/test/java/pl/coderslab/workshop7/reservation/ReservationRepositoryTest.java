package pl.coderslab.workshop7.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.user.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository repository;

    @MockitoBean
    private Clock clock;

    private User user;
    private Accommodation accommodation;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC"));
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());

        user = new User("test", "test@gmail.com", "test");
        entityManager.persist(user);

        accommodation = new Accommodation();
        accommodation.setName("test");
        entityManager.persist(accommodation);

        reservation1 = new Reservation();
        reservation1.setUser(user);
        reservation1.setAccommodation(accommodation);
        reservation1.setReservationStart(LocalDate.of(2020, 1, 1));
        reservation1.setReservationEnd(LocalDate.of(2020, 2, 1));
        entityManager.persist(reservation1);

        reservation2 = new Reservation();
        reservation2.setUser(user);
        reservation2.setAccommodation(accommodation);
        reservation2.setReservationStart(LocalDate.of(2025, 5, 1));
        reservation2.setReservationEnd(LocalDate.of(2025, 5, 10));
        entityManager.persist(reservation2);
    }

    @Test
    void findAllByUserIdTest() {
        List<Reservation> foundReservation = repository.findAllByUserId(user.getId());

        assertThat(foundReservation)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(reservation1, reservation2);

        List<Reservation> foundReservation2 = repository.findAllByUserId(3L);
        assertThat(foundReservation2)
                .isEmpty();
    }

    @Test
    void findPastReservationsByUserIdTest() {
        List<Reservation> foundReservation = repository.findPastReservationsByUserId(user.getId(), LocalDate.now(clock));

        assertThat(foundReservation)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation1)
                .doesNotContain(reservation2);
    }

    @Test
    void findCurrentReservationsByUserIdTest() {
        List<Reservation> foundReservation = repository.findCurrentReservationsByUserId(user.getId(), LocalDate.now(clock));

        assertThat(foundReservation)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation2)
                .doesNotContain(reservation1);
    }

}