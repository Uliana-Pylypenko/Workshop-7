package pl.coderslab.workshop7.festival;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FestivalRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FestivalRepository repository;

    private Festival festival1;
    private Festival festival2;
    private Festival festival3;

    @BeforeEach
    void setUp() {
        festival1 = new Festival();
        festival1.setStartDate(LocalDate.of(2020, 1, 1));
        festival1.setName("Festival1");
        festival1.setLocation("Warszawa");
        festival1.setPricePerDay(6.0);
        entityManager.persist(festival1);

        festival2 = new Festival();
        festival2.setStartDate(LocalDate.of(2027, 1, 2));
        festival2.setName("Festival2");
        festival2.setLocation("Warka");
        festival2.setPricePerDay(7.0);
        entityManager.persist(festival2);

        festival3 = new Festival();
        festival3.setStartDate(LocalDate.of(2026, 1, 2));
        festival3.setName("Festival3");
        festival3.setLocation("Krakow");
        festival3.setPricePerDay(8.0);
        entityManager.persist(festival3);
    }

    @Test
    void findUpcomingFestivalsTest() {
        List<Festival> upcomingFestivals = repository.findUpcomingFestivals();

        assertThat(upcomingFestivals)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(festival3, festival2);
    }

    @Test
    void findOneByNameTest() {

        List<Festival> foundFestivals = repository.findAllByNameContainingIgnoreCase(festival1.getName());
        assertThat(foundFestivals)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival1);

        List<Festival> foundFestivals2 = repository.findAllByNameContainingIgnoreCase("festival");
        assertThat(foundFestivals2)
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(festival1, festival2, festival3);
    }

    @Test
    void findAllByLocationTest() {
        List<Festival> festivalsInWarsaw = repository.findAllByLocationContainingIgnoreCase("Warszawa");
        assertThat(festivalsInWarsaw)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival1);

        List<Festival> festivalsInWar = repository.findAllByLocationContainingIgnoreCase("war");
        assertThat(festivalsInWar)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(festival1, festival2);
    }

    @Test
    void findAllByStartDateBetweenTest() {
        List<Festival> festivalsIn2026 = repository.findAllByStartDateBetween(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        assertThat(festivalsIn2026)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival3);
    }


    @Test
    void findAllByPricePerDayBetween() {
        List<Festival> pricesBetween5and10 = repository.findAllByPricePerDayBetween(5.0, 10.0);

        assertThat(pricesBetween5and10)
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(festival1, festival2, festival3);

        List<Festival> pricesBetween8and10 = repository.findAllByPricePerDayBetween(8.0, 10.0);

        assertThat(pricesBetween8and10)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(festival3);

        List<Festival> pricesBetween10and20 = repository.findAllByPricePerDayBetween(10.0, 20.0);

        assertThat(pricesBetween10and20)
                .isEmpty();
    }

}