package pl.coderslab.workshop7.accommodation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Accommodation> accommodation = repository.findById(id);
        if (accommodation.isPresent()) {
            return accommodation.get();
        } else {
            throw new EntityNotFoundException("Accommodation with id " + id + " not found");
        }
    }

    @Override
    public Accommodation save(Accommodation accommodation) {
        return repository.save(accommodation);
    }
}
