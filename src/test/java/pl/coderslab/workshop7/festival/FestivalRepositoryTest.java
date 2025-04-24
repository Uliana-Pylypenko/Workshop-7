package pl.coderslab.workshop7.festival;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import pl.coderslab.workshop7.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FestivalRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FestivalRepository repository;

    @Test
    void findUpcomingFestivalsTest() {
        Festival festival1 = new Festival();
        festival1.setStartDate(LocalDate.of(2020, 1, 1));
        entityManager.persist(festival1);
        Festival festival2 = new Festival();
        festival2.setStartDate(LocalDate.of(2027, 1, 2));
        entityManager.persist(festival2);
        Festival festival3 = new Festival();
        festival3.setStartDate(LocalDate.of(2026, 1, 2));
        entityManager.persist(festival3);

        List<Festival> upcomingFestivals = repository.findUpcomingFestivals();

        assertThat(upcomingFestivals)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(festival3, festival2);

    }

}