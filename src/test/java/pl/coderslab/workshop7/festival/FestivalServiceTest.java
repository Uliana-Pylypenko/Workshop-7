package pl.coderslab.workshop7.festival;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FestivalServiceTest {
    @InjectMocks
    private FestivalServiceImpl service;

    @Mock
    private FestivalRepository repository;

    @Test
    void givenFestivalList_whenGetUpcomingFestivalsByCategory_thenReturnFestivalList() {
        List<Festival> festivalList = new ArrayList<>();

        Festival festival1 = new Festival();
        festival1.setName("Festival1");
        festival1.setStartDate(LocalDate.of(2026, 1, 1));
        festival1.setFestivalCategory(FestivalCategory.FILM);

        Festival festival2 = new Festival();
        festival2.setName("Festival2");
        festival2.setStartDate(LocalDate.of(2025, 7, 2));
        festival2.setFestivalCategory(FestivalCategory.SCIENCE);

        Festival festival3 = new Festival();
        festival3.setName("Festival3");
        festival3.setStartDate(LocalDate.of(2025, 7, 3));
        festival3.setFestivalCategory(FestivalCategory.MUSIC);

        festivalList.add(festival1);
        festivalList.add(festival2);
        festivalList.add(festival3);
        when(repository.findUpcomingFestivals()).thenReturn(festivalList);

        assertThat(service.getUpcomingFestivalsByCategory(FestivalCategory.FILM))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival1);

        assertThat(service.getUpcomingFestivalsByCategory(FestivalCategory.SCIENCE))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival2);

        assertThat(service.getUpcomingFestivalsByCategory(FestivalCategory.MUSIC))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival3);


    }

}