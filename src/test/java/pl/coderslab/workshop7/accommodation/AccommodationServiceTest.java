package pl.coderslab.workshop7.accommodation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.workshop7.festival.Festival;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {
    @InjectMocks
    private AccommodationServiceImpl service;

    @Mock
    private AccommodationRepository repository;

    private List<Accommodation> accommodationList;
    private Accommodation accommodation1;
    private Accommodation accommodation2;

    @BeforeEach
    void setUp() {
        accommodationList = new ArrayList<>();

        accommodation1 = new Accommodation();
        accommodation1.setId(1L);
        accommodation1.setLocation("Warszawa");
        accommodation1.setPricePerDay(7.0);
        Festival festival1 = new Festival();
        festival1.setId(1L);
        accommodation1.setFestival(festival1);

        accommodation2 = new Accommodation();
        accommodation2.setId(2L);
        accommodation2.setLocation("Krakow");
        accommodation2.setPricePerDay(15.0);

        accommodationList.add(accommodation1);
        accommodationList.add(accommodation2);
    }

    @Test
    void givenAccommodationList_whenFindByLocation_thenReturnAccommodationList() {
        when(repository.findByLocationIgnoreCase("warszawa")).thenReturn(List.of(accommodation1));

        assertThat(service.findByLocation("warszawa"))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }

    @Test
    void givenAccommodationList_whenFindByNonExistentLocation_thenReturnEmptyList() {
        when(repository.findByLocationIgnoreCase("Gdansk")).thenReturn(List.of());

        assertThat(service.findByLocation("Gdansk"))
                .isEmpty();
    }

    @Test
    void givenAccommodationList_whenFindByPricePerDayBetween_thenReturnAccommodationList() {
        double priceLow1 = 0.0;
        double priceHigh1 = 20.0;
        when(repository.findByPricePerDayBetween(priceLow1, priceHigh1)).thenReturn(accommodationList);

        assertThat(service.findByPricePerDayBetween(priceLow1, priceHigh1))
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(accommodation1, accommodation2);

        double priceLow2 = 5.0;
        double priceHigh2 = 10.0;
        when(repository.findByPricePerDayBetween(priceLow2, priceHigh2)).thenReturn(List.of(accommodation1));

        assertThat(service.findByPricePerDayBetween(priceLow2, priceHigh2))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }

    @Test
    void whenFindByPricePerDayBetween_thenReturnEmptyList() {
        double priceLow = 20.0;
        double priceHigh = 30.0;
        when(repository.findByPricePerDayBetween(priceLow, priceHigh)).thenReturn(List.of());

        assertThat(service.findByPricePerDayBetween(priceLow, priceHigh))
                .isEmpty();
    }

    @Test
    void whenFindByFestivalId_thenReturnAccommodationList() {
        when(repository.findByFestivalId(1L)).thenReturn(List.of(accommodation1));

        assertThat(service.findByFestivalId(1L))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }

    @Test
    void whenFindByFestivalId_thenReturnEmptyList() {
        when(repository.findByFestivalId(2L)).thenReturn(List.of());

        assertThat(service.findByFestivalId(2L))
                .isEmpty();
    }

    @Test
    void whenFindById_thenReturnAccommodation() {
        when(repository.findById(1L)).thenReturn(Optional.of(accommodation1));

        assertThat(service.findById(1L))
                .isNotNull()
                .isEqualTo(accommodation1);

        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThat(service.findById(3L))
            .isNull();
    }
}