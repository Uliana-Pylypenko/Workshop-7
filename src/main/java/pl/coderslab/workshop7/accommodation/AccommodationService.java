package pl.coderslab.workshop7.accommodation;

import java.util.List;

public interface AccommodationService {
    List<Accommodation> findByLocation(String location);
}
