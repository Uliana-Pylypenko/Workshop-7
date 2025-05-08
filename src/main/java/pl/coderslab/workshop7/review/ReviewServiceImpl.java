package pl.coderslab.workshop7.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.accommodation.AccommodationRepository;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.festival.FestivalRepository;
import pl.coderslab.workshop7.user.User;
import pl.coderslab.workshop7.user.UserRepository;

import java.util.List;


@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private FestivalRepository festivalRepository;
    private AccommodationRepository accommodationRepository;
    private UserRepository userRepository;

    @Override
    public Review addFestivalReview(Long userId, Long festivalId, int rating, String comment) {
        return addReview(userId, festivalId, rating, comment, ReviewType.FESTIVAL);
    }

    @Override
    public Review addAccommodationReview(Long userId, Long accommodationId, int rating, String comment) {
        return addReview(userId, accommodationId, rating, comment, ReviewType.ACCOMMODATION);
    }

    private Review addReview(Long userId, Long entityId, int rating, String comment, ReviewType reviewType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User must login"));

        Review review;
        if (reviewType == ReviewType.FESTIVAL) {
            Festival festival = festivalRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Festival not found"));
            review = new Review(user, festival, rating, comment);
        } else {
            Accommodation accommodation = accommodationRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));
            review = new Review(user, accommodation, rating, comment);
        }

        return reviewRepository.save(review);
    }

    private enum ReviewType {
        FESTIVAL, ACCOMMODATION
    }

    @Override
    public List<Review> findAllByFestivalId(Long festivalId) {
        if (festivalRepository.findById(festivalId).isPresent()) {
            return reviewRepository.findAllByFestivalId(festivalId);
        } else {
            throw new EntityNotFoundException("Festival not found");
        }
    }

    @Override
    public List<Review> findAllByAccommodationId(Long accommodationId) {
        if (accommodationRepository.findById(accommodationId).isPresent()) {
            return reviewRepository.findAllByAccommodationId(accommodationId);
        } else {
            throw new EntityNotFoundException("Accommodation not found");
        }
    }
}
