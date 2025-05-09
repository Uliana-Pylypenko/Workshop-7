package pl.coderslab.workshop7.festival;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;
    private Clock clock;

    @Override
    public List<Festival> getUpcomingFestivalsByCategory(FestivalCategory category) {
        List<Festival> upcomingFestivals = festivalRepository.findUpcomingFestivals(LocalDate.now(clock));
        return upcomingFestivals
                .stream()
                .filter(festival -> festival.getFestivalCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Festival> findAllByNameContainingIgnoreCase(String name) {
        return festivalRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Festival> findAllByLocationContainingIgnoreCase(String location) {
        return festivalRepository.findAllByLocationContainingIgnoreCase(location);
    }

    @Override
    public List<Festival> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(endDate)) {
            return festivalRepository.findAllByStartDateBetween(startDate, endDate);
        } else {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    @Override
    public List<Festival> findAllByPricePerDayBetween(double lower, double higher) {
        if (lower <= higher) {
            return festivalRepository.findAllByPricePerDayBetween(lower, higher);
        } else {
            throw new IllegalArgumentException("Lower date must be before higher date");
        }
    }

    @Override
    public Festival getFestivalById(Long id) {
        return festivalRepository.findById(id).orElse(null);
    }

    @Override
    public Festival save(Festival festival) {
        return festivalRepository.save(festival);
    }

    @Override
    public Festival findOneByName(String name) {
        Optional<Festival> festivalOptional = festivalRepository.findOneByName(name);
        if (festivalOptional.isPresent()) {
            return festivalOptional.get();
        } else {
            throw new EntityNotFoundException("Festival with name " + name + " not found");
        }
    }
}
