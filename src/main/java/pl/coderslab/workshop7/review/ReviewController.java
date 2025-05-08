package pl.coderslab.workshop7.review;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add/festival")
    public ResponseEntity<Review> addFestivalReview(@RequestParam Long userId,
                                                    @RequestParam Long festivalId,
                                                    @RequestParam int rating,
                                                    @RequestParam String comment) {

        Review review = reviewService.addFestivalReview(userId, festivalId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/add/accommodation")
    public ResponseEntity<Review> addAccommodationReview(@RequestParam Long userId,
                                                    @RequestParam Long accommodationId,
                                                    @RequestParam int rating,
                                                    @RequestParam String comment) {

        Review review = reviewService.addAccommodationReview(userId, accommodationId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/festival/{festivalId}")
    public ResponseEntity<List<Review>> getReviewByFestivalId(@PathVariable Long festivalId) {
        List<Review> reviews = reviewService.findAllByFestivalId(festivalId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
