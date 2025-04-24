package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

}
