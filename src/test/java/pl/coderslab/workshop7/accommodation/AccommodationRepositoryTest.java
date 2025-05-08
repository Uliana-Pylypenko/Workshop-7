package pl.coderslab.workshop7.accommodation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.workshop7.festival.Festival;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccommodationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccommodationRepository repository;

    private Accommodation accommodation1;
    private Accommodation accommodation2;

    @BeforeEach
    void setUp() {
        accommodation1 = new Accommodation();
        accommodation1.setLocation("Warszawa");
        accommodation1.setPricePerDay(5.0);
        Festival festival1 = new Festival();
        festival1.setName("Festival1");
        accommodation1.setFestival(festival1);
        entityManager.persist(festival1);
        entityManager.persist(accommodation1);

        accommodation2 = new Accommodation();
        accommodation2.setLocation("Krakow");
        accommodation2.setPricePerDay(15.0);
        entityManager.persist(accommodation2);
    }

    @Test
    void whenFindByLocationIgnoreCase_thenReturnListOfAccommodation1() {
        List<Accommodation> accommodationsInWarsaw = repository.findByLocationIgnoreCase("warszawa");

        assertThat(accommodationsInWarsaw)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }

    @Test
    void whenFindByPricePerDayBetween_thenReturnListOfAccommodations() {
        List<Accommodation> priceBetween5and10 = repository.findByPricePerDayBetween(5.0, 10.0);

        assertThat(priceBetween5and10)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);

        List<Accommodation> priceBetween0and20 = repository.findByPricePerDayBetween(0.0, 20.0);

        assertThat(priceBetween0and20)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(accommodation1, accommodation2);
    }

    @Test
    void whenFindByFestivalId_thenReturnListOfAccommodation1() {
        Long festivalId = accommodation1.getFestival().getId();
        assertThat(repository.findByFestivalId(festivalId))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);

        assertThat(repository.findByFestivalId(2L))
                .isEmpty();
    }



}