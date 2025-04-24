package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;

    @Override
    public List<Festival> getUpcomingFestivalsByCategory(FestivalCategory category) {
        return null;
    }

}
