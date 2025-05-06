package pl.coderslab.workshop7.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.user.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository repository;

    @Test
    void findAllByUserIdTest() {
        User user = new User("test", "test", "test");
        entityManager.persist(user);

        Accommodation accommodation = new Accommodation();
        accommodation.setName("test");
        entityManager.persist(accommodation);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setAccommodation(accommodation);
        reservation.setReservationStart(LocalDate.of(2020, 1, 1));
        reservation.setReservationEnd(LocalDate.of(2020, 2, 1));
        entityManager.persist(reservation);

        List<Reservation> foundReservation = repository.findAllByUserId(user.getId());
        assertThat(foundReservation)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(reservation);

        List<Reservation> foundReservation2 = repository.findAllByUserId(2L);
        assertThat(foundReservation2)
                .isEmpty();
    }
}