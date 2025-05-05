package pl.coderslab.workshop7.accommodation;

import java.util.List;

public interface AccommodationService {
    List<Accommodation> findByLocation(String location);

    List<Accommodation> findByPricePerDayBetween(Double lower, Double higher);

    List<Accommodation> findByFestivalId(Long id);

    Accommodation findById(Long id);
}
