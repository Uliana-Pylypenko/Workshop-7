package pl.coderslab.workshop7.festival;

import java.util.List;

public interface FestivalService {
    List<Festival> getUpcomingFestivalsByCategory(FestivalCategory category);
}
