package pl.coderslab.workshop7.review;

import java.util.List;

public interface ReviewService {
    Review addFestivalReview(Long userId, Long festivalId, int rating, String comment);

    Review addAccommodationReview(Long userId, Long accommodationId, int rating, String comment);

    List<Review> findAllByFestivalId(Long festivalId);
}
