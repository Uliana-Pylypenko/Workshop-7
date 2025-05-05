package pl.coderslab.workshop7.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByLocationIgnoreCase(String location);

    List<Accommodation> findByPricePerDayBetween(Double lower, Double higher);

    //@Query("select a from Accommodation a where a.festival.id=?1")
    List<Accommodation> findByFestivalId(Long id);

}
