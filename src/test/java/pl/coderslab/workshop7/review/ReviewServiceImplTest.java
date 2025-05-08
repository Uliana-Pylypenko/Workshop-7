package pl.coderslab.workshop7.review;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.festival.FestivalRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @InjectMocks
    private ReviewServiceImpl service;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private FestivalRepository festivalRepository;

    private User user;
    private Festival festival;
    private Review festivalReview;
    private Accommodation accommodation;
    private Review accommodationReview;
    private int rating;
    private String comment;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@gmail.com", "password");

        festival = new Festival();
        festival.setId(1L);
        festival.setName("Festival");

        accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setName("Accommodation");

        rating = 5;
        comment = "This is a comment";
        festivalReview = new Review(user, festival, rating, comment);
        accommodationReview = new Review(user, accommodation, rating, comment);
    }

    @Test
    void whenAddFestivalReview_thenReturnReview() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(festivalRepository.findById(festival.getId())).thenReturn(Optional.of(festival));
        when(reviewRepository.save(any(Review.class))).thenReturn(festivalReview);

        Review addedReview = service.addFestivalReview(user.getId(), festival.getId(), rating, comment);

        assertThat(addedReview)
                .isNotNull()
                .isEqualTo(festivalReview);
    }

    @Test
    void givenNonExistingUser_whenAddFestivalReview_thenThrowEntityNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                service.addFestivalReview(user.getId(), festival.getId(), rating, comment));
        assertThat(entityNotFoundException.getMessage()).isEqualTo("User must login");
    }

    @Test
    void givenNonExistingFestival_whenAddFestivalReview_thenThrowEntityNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(festivalRepository.findById(festival.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                        service.addFestivalReview(user.getId(), festival.getId(), rating, comment));

        assertThat(entityNotFoundException.getMessage()).isEqualTo("Festival not found");
    }

    @Test
    void whenAddAccommodationReview_thenReturnReview() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        when(reviewRepository.save(any(Review.class))).thenReturn(accommodationReview);

        Review review = service.addAccommodationReview(user.getId(), accommodation.getId(), rating, comment);

        assertThat(review)
                .isNotNull()
                .isEqualTo(accommodationReview);
    }

    @Test
    void givenNonExistingUser_whenAddAccommodationReview_thenThrowEntityNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                        service.addAccommodationReview(user.getId(), accommodation.getId(), rating, comment));

        assertThat(entityNotFoundException.getMessage()).isEqualTo("User must login");
    }

    @Test
    void givenNonExistingAccommodation_whenAddFestivalReview_thenThrowEntityNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                        service.addAccommodationReview(user.getId(), accommodation.getId(), rating, comment));

        assertThat(entityNotFoundException.getMessage()).isEqualTo("Accommodation not found");
    }

    @Test
    void whenFindAllByFestivalId_thenReturnReviewList() {
        when(festivalRepository.findById(festival.getId())).thenReturn(Optional.of(festival));
        when(reviewRepository.findAllByFestivalId(festival.getId())).thenReturn(List.of(festivalReview));

        List<Review> reviews = service.findAllByFestivalId(festival.getId());

        assertThat(reviews)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festivalReview);
    }

    @Test
    void givenNonExistingFestival_whenFindAllByFestivalId_thenThrowEntityNotFoundException() {
        when(festivalRepository.findById(festival.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                service.findAllByFestivalId(festival.getId()));

        assertThat(entityNotFoundException.getMessage()).isEqualTo("Festival not found");
    }


    @Test
    void whenFindAllByAccommodationId_thenReturnReviewList() {
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        when(reviewRepository.findAllByAccommodationId(accommodation.getId())).thenReturn(List.of(accommodationReview));

        List<Review> reviews = service.findAllByAccommodationId(accommodation.getId());

        assertThat(reviews)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodationReview);
    }

    @Test
    void givenNonExistingAccommodation_whenFindAllByAccommodationId_thenThrowEntityNotFoundException() {
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () ->
                service.findAllByAccommodationId(accommodation.getId()));

        assertThat(entityNotFoundException.getMessage()).isEqualTo("Accommodation not found");
    }

}