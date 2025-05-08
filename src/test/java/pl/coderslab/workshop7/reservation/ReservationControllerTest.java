package pl.coderslab.workshop7.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.coderslab.workshop7.user.User;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {

        reservation1 = new Reservation();
        reservation1.setId(1L);
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);
        reservation1.setReservationStart(reservationStart);
        reservation1.setReservationEnd(reservationEnd);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        LocalDate reservationStart2 = LocalDate.of(2025, 1, 1);
        LocalDate reservationEnd2 = LocalDate.of(2025, 2, 1);
        reservation2.setReservationStart(reservationStart2);
        reservation2.setReservationEnd(reservationEnd2);
    }

    @Test
    void whenCreateReservation_thenReturnReservation() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2020, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 2, 1);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd))
                .thenReturn(reservation1);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                .param("userId", String.valueOf(userId))
                .param("accommodationId", String.valueOf(accommodationId))
                .param("reservationStartString", String.valueOf(reservationStart))
                .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationStart").value(String.valueOf(reservationStart)))
                .andExpect(jsonPath("$.reservationEnd").value(String.valueOf(reservationEnd)));
    }

    @Test
    void whenCreateReservation_thenCatchDateTimeParseException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        String reservationStart = "20.04.2024";
        String reservationEnd = "20.05.2025";

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                .param("userId", String.valueOf(userId))
                .param("accommodationId", String.valueOf(accommodationId))
                .param("reservationStartString", reservationStart)
                .param("reservationEndString", reservationEnd))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateReservation_thenCatchIllegalArgumentException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2025, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 1, 1);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                        .param("userId", String.valueOf(userId))
                        .param("accommodationId", String.valueOf(accommodationId))
                        .param("reservationStartString", String.valueOf(reservationStart))
                        .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateReservation_thenCatchEntityNotFoundException() throws Exception {
        Long userId = 1L;
        Long accommodationId = 1L;
        LocalDate reservationStart = LocalDate.of(2025, 1, 1);
        LocalDate reservationEnd = LocalDate.of(2020, 1, 1);

        when(reservationService.create(userId, accommodationId, reservationStart, reservationEnd)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/create")
                        .param("userId", String.valueOf(userId))
                        .param("accommodationId", String.valueOf(accommodationId))
                        .param("reservationStartString", String.valueOf(reservationStart))
                        .param("reservationEndString", String.valueOf(reservationEnd)))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllByUserId_thenReturnListOfReservations() throws Exception {
        Long userId = 1L;
        when(reservationService.findAllByUserId(userId)).thenReturn(List.of(reservation1, reservation2));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/all/1"))

                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(reservation1.getId().toString()))
                .andExpect(jsonPath("$[0].reservationStart").value(reservation1.getReservationStart().toString()))
                .andExpect(jsonPath("$[0].reservationEnd").value(reservation1.getReservationEnd().toString()))

                .andExpect(jsonPath("$[1].id").value(reservation2.getId().toString()))
                .andExpect(jsonPath("$[1].reservationStart").value(reservation2.getReservationStart().toString()))
                .andExpect(jsonPath("$[1].reservationEnd").value(reservation2.getReservationEnd().toString()));
    }

    @Test
    void whenGetAllByNonExistentUserId_thenThrowEntityNotFoundException() throws Exception {
        when(reservationService.findAllByUserId(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/all/1"))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetPastReservations_thenReturnPastReservation() throws Exception {
        Long userId = 1L;
        when(reservationService.findPastReservationsByUserId(userId)).thenReturn(List.of(reservation1));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/past/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation1.getId().toString()))
                .andExpect(jsonPath("$[0].reservationStart").value(reservation1.getReservationStart().toString()))
                .andExpect(jsonPath("$[0].reservationEnd").value(reservation1.getReservationEnd().toString()));
    }

    @Test
    void whenGetPastReservations_thenCatchEntityNotFoundException() throws Exception {
        Long userId = 1L;
        when(reservationService.findPastReservationsByUserId(userId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/past/1"))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetCurrentReservation_thenReturnCurrentReservation() throws Exception {
        Long userId = 1L;
        when(reservationService.findCurrentReservationsByUserId(userId)).thenReturn(List.of(reservation2));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/current/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation2.getId().toString()))
                .andExpect(jsonPath("$[0].reservationStart").value(reservation2.getReservationStart().toString()))
                .andExpect(jsonPath("$[0].reservationEnd").value(reservation2.getReservationEnd().toString()));
    }

    @Test
    void whenGetCurrentReservation_thenCatchEntityNotFoundException() throws Exception {
        Long userId = 1L;
        when(reservationService.findCurrentReservationsByUserId(userId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/current/1"))

                .andDo(print())
                .andExpect(status().isNotFound());
    }




}