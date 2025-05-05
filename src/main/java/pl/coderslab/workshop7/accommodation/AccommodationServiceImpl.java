package pl.coderslab.workshop7.accommodation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private AccommodationRepository repository;

    @Override
    public List<Accommodation> findByLocation(String location) {
        return repository.findByLocationIgnoreCase(location);
    }

    @Override
    public List<Accommodation> findByPricePerDayBetween(Double lower, Double higher) {
        return repository.findByPricePerDayBetween(lower, higher);
    }

    @Override
    public List<Accommodation> findByFestivalId(Long id) {
        return repository.findByFestivalId(id);
    }

    @Override
    public Accommodation findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
