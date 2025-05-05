package pl.coderslab.workshop7.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByLocation(String location);
}
