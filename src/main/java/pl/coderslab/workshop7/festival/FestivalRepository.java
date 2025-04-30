package pl.coderslab.workshop7.festival;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    @Query("select f from Festival f where f.startDate > current_date order by f.startDate asc")
    List<Festival> findUpcomingFestivals();

    List<Festival> findAllByNameContainingIgnoreCase(String name);

    List<Festival> findAllByLocationContainingIgnoreCase(String location);

    List<Festival> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
