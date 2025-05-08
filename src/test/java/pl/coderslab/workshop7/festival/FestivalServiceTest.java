package pl.coderslab.workshop7.festival;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FestivalServiceTest {
    @InjectMocks
    private FestivalServiceImpl service;

    @Mock
    private FestivalRepository repository;

    @Mock
    private Clock clock;

    private List<Festival> festivalList;
    private Festival festival1;
    private Festival festival2;
    private Festival festival3;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC"));

        festivalList = new ArrayList<>();

        festival1 = new Festival();
        festival1.setName("Festival1");
        festival1.setStartDate(LocalDate.of(2026, 1, 1));
        festival1.setFestivalCategory(FestivalCategory.FILM);
        festival1.setLocation("Warszawa");
        festival1.setPricePerDay(6.0);

        festival2 = new Festival();
        festival2.setName("Festival2");
        festival2.setStartDate(LocalDate.of(2025, 7, 2));
        festival2.setFestivalCategory(FestivalCategory.SCIENCE);
        festival2.setLocation("Warka");
        festival2.setPricePerDay(7.0);

        festival3 = new Festival();
        festival3.setName("Festival3");
        festival3.setStartDate(LocalDate.of(2025, 7, 3));
        festival3.setFestivalCategory(FestivalCategory.MUSIC);
        festival3.setLocation("Krakow");
        festival3.setPricePerDay(8.0);

        festivalList.add(festival1);
        festivalList.add(festival2);
        festivalList.add(festival3);
    }


    @Test
    void whenGetUpcomingFestivalsByCategory_thenReturnFestivalList() {
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(repository.findUpcomingFestivals(LocalDate.now(clock))).thenReturn(festivalList);

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

    @Test
    void whenFindAllByNameContainingIgnoreCase_thenReturnFestivals() {
        when(repository.findAllByNameContainingIgnoreCase("Festival1")).thenReturn(List.of(festival1));
        when(repository.findAllByNameContainingIgnoreCase("festival")).thenReturn(festivalList);

        assertThat(service.findAllByNameContainingIgnoreCase("Festival1"))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival1);

        assertThat(service.findAllByNameContainingIgnoreCase("festival"))
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(festival1, festival2, festival3);
    }

    @Test
    void whenFindAllByLocationContainingIgnoreCase_thenReturnFestivals() {
        when(repository.findAllByLocationContainingIgnoreCase("Warszawa")).thenReturn(List.of(festival1));
        when(repository.findAllByLocationContainingIgnoreCase("war")).thenReturn(List.of(festival1, festival2));

        assertThat(service.findAllByLocationContainingIgnoreCase("Warszawa"))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival1);

        assertThat(service.findAllByLocationContainingIgnoreCase("war"))
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(festival1, festival2);
    }

    @Test
    void whenFindAllByStartDateBetween_thenReturnFestivals() {
        LocalDate startDate1 = LocalDate.of(2026, 1, 1);
        LocalDate startDate2 = LocalDate.of(2027, 7, 2);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        when(repository.findAllByStartDateBetween(startDate1, endDate)).thenReturn(List.of(festival3));

        assertThat(service.findAllByStartDateBetween(startDate1, endDate))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(festival3);

        assertThrows(IllegalArgumentException.class, () -> service.findAllByStartDateBetween(startDate2, endDate));
    }


    @Test
    void whenGetDetailsById_thenReturnFestival() {
        when(repository.findById(1L)).thenReturn(Optional.of(festival1));

        assertThat(service.getFestivalById(1L))
                .isNotNull()
                .isEqualTo(festival1);
    }

    @Test
    void whenFindAllByPricePerDayBetween_thenReturnFestivals() {
        double priceLower1 = 5.0;
        double priceHigher1 = 10.0;
        when(repository.findAllByPricePerDayBetween(priceLower1, priceHigher1)).thenReturn(festivalList);

        assertThat(service.findAllByPricePerDayBetween(priceLower1, priceHigher1))
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(festival1, festival2, festival3);

        double priceLower2 = 10.0;
        double priceHigher2 = 20.0;
        when(repository.findAllByPricePerDayBetween(priceLower2, priceHigher2)).thenReturn(List.of());

        assertThat(service.findAllByPricePerDayBetween(priceLower2, priceHigher2))
                .isEmpty();
    }

    @Test
    void givenPriceLowerIsHigher_whenFindAllByPricePerDayBetween_thenThrowException() {
        double priceLower = 10.0;
        double priceHigher = 5.0;

        assertThrows(IllegalArgumentException.class, () -> service.findAllByPricePerDayBetween(priceLower, priceHigher));
    }
}