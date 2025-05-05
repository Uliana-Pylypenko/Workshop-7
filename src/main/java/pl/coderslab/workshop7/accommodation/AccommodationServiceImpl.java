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
        return repository.findByLocation(location);
    }
}
