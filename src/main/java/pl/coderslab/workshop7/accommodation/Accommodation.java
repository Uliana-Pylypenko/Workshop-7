package pl.coderslab.workshop7.accommodation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.review.Review;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Festival festival;

    private String name;
    private String description;
    private double pricePerDay;
    private String location;
    private String imageUrl;

    @OneToMany(mappedBy = "accommodation")
    private List<Review> reviews = new ArrayList<>();

    @ElementCollection(targetClass = Amenity.class)
    @CollectionTable(name = "accommodation_amenities", joinColumns = @JoinColumn(name = "accommodation_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    private Set<Amenity> amenities = new HashSet<>();
}
