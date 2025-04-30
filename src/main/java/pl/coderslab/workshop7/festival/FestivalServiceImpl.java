package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;

    @Override
    public List<Festival> getUpcomingFestivalsByCategory(FestivalCategory category) {
        List<Festival> upcomingFestivals = festivalRepository.findUpcomingFestivals();
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
}
