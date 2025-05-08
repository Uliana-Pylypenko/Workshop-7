package pl.coderslab.workshop7.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.user.User;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Festival festival;

    @ManyToOne
    private Accommodation accommodation;

    @Min(0)
    @Max(10)
    private int rating;

    private String comment;

    public Review(User user, Festival festival, int rating, String comment) {
        this.user = user;
        this.festival = festival;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(User user, Accommodation accommodation, int rating, String comment) {
        this.user = user;
        this.accommodation = accommodation;
        this.rating = rating;
        this.comment = comment;
    }
}
