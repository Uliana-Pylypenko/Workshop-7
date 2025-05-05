package pl.coderslab.workshop7.accommodation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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
        accommodation1.setLocation("Warszawa");

        accommodation2 = new Accommodation();
        accommodation2.setLocation("Krakow");

        accommodationList.add(accommodation1);
        accommodationList.add(accommodation2);
    }

    @Test
    void givenAccommodationList_whenFindByLocation_thenReturnAccommodationList() {
        when(repository.findByLocation("Warszawa")).thenReturn(List.of(accommodation1));

        assertThat(service.findByLocation("Warszawa"))
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(accommodation1);
    }

    @Test
    void givenAccommodationList_whenFindByNonExistentLocation_thenReturnEmptyList() {
        when(repository.findByLocation("Gdansk")).thenReturn(List.of());

        assertThat(service.findByLocation("Gdansk"))
                .isEmpty();
    }
    

}