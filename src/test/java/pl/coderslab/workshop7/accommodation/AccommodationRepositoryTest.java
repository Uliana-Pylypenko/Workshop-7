package pl.coderslab.workshop7.accommodation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
        entityManager.persist(accommodation1);

        accommodation2 = new Accommodation();
        accommodation2.setLocation("Krakow");
        entityManager.persist(accommodation2);
    }

    @Test
    void findByLocationTest() {
        List<Accommodation> accommodationsInWarsaw = repository.findByLocationIgnoreCase("warszawa");

        assertThat(accommodationsInWarsaw)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }



}