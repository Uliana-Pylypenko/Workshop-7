package pl.coderslab.workshop7.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.user.User;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
class ReviewRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository repository;

    private User user;
    private Festival festival;
    private Accommodation accommodation;
    private Review festivalReview;
    private Review accommodationReview;
    private int rating;
    private String comment;

    @BeforeEach
    void setUp() {
        user = new User("user", "user@email.com", "password");
        entityManager.persist(user);

        festival = new Festival();
        festival.setName("Festival");
        entityManager.persist(festival);

        accommodation = new Accommodation();
        accommodation.setName("Accommodation");
        entityManager.persist(accommodation);

        rating = 5;
        comment = "This is a comment";
        festivalReview = new Review(user, festival, rating, comment);
        entityManager.persist(festivalReview);

        accommodationReview = new Review(user, accommodation, rating, comment);
        entityManager.persist(accommodationReview);
    }

    @Test
    void whenFindAllByFestivalId_thenReturnListOfFestivalReviews() {
        List<Review> reviewList = repository.findAllByFestivalId(festival.getId());

        assertThat(reviewList)
                .isNotEmpty()
                .hasSize(1);

        Review review = reviewList.get(0);
        assertThat(review.getUser().getId()).isEqualTo(user.getId());
        assertThat(review.getFestival().getId()).isEqualTo(festival.getId());
        assertThat(review.getComment()).isEqualTo(comment);
        assertThat(review.getRating()).isEqualTo(rating);
    }

    @Test
    void whenFindAllByAccommodationId_thenReturnListOfAccommodationReviews() {
        List<Review> reviewList = repository.findAllByAccommodationId(accommodation.getId());

        assertThat(reviewList)
                .isNotEmpty()
                .hasSize(1);

        Review review = reviewList.get(0);
        assertThat(review.getUser().getId()).isEqualTo(user.getId());
        assertThat(review.getAccommodation().getId()).isEqualTo(accommodation.getId());
        assertThat(review.getComment()).isEqualTo(comment);
        assertThat(review.getRating()).isEqualTo(rating);
    }

}