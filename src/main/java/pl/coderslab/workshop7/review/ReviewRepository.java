package pl.coderslab.workshop7.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByFestivalId(Long festivalId);

    List<Review> findAllByAccommodationId(Long accommodationId);
}
