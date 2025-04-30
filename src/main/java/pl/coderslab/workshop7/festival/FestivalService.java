package pl.coderslab.workshop7.festival;

import java.time.LocalDate;
import java.util.List;

public interface FestivalService {
    List<Festival> getUpcomingFestivalsByCategory(FestivalCategory category);

    List<Festival> findAllByNameContainingIgnoreCase(String name);

    List<Festival> findAllByLocationContainingIgnoreCase(String location);

    List<Festival> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
