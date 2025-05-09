package pl.coderslab.workshop7.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByLocationIgnoreCase(String location);

    List<Accommodation> findByPricePerDayBetween(Double lower, Double higher);

    List<Accommodation> findByFestivalId(Long id);

    Optional<Accommodation> findOneByName(String name);

}
