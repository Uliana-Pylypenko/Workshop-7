package pl.coderslab.workshop7.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.festival.Festival;
import pl.coderslab.workshop7.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Festival festival;
    private Review festivalReview;
    private Accommodation accommodation;
    private Review accommodationReview;
    private int rating;
    private String comment;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@gmail.com", "password");

        festival = new Festival();
        festival.setId(1L);
        festival.setName("Festival");

        accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setName("Accommodation");

        rating = 5;
        comment = "This is a comment";
        festivalReview = new Review(user, festival, rating, comment);
        accommodationReview = new Review(user, accommodation, rating, comment);
    }

    @Test
    void whenAddFestivalReview_thenReturnReview() throws Exception {
        when(reviewService.addFestivalReview(user.getId(), festival.getId(), rating, comment))
                .thenReturn(festivalReview);

        mockMvc.perform(post("/review/add/festival")
                .param("userId", Long.toString(user.getId()))
                .param("festivalId", Long.toString(festival.getId()))
                .param("rating", Integer.toString(rating))
                .param("comment", comment))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(user.getId()))
                .andExpect(jsonPath("$.festival.id").value(festival.getId()))
                .andExpect(jsonPath("$.rating").value(rating))
                .andExpect(jsonPath("$.comment").value(comment));
    }

    @Test
    void whenAddFestivalReview_thenThrowEntityNotFoundException() throws Exception {
        when(reviewService.addFestivalReview(user.getId(), festival.getId(), rating, comment))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/review/add/festival")
                        .param("userId", Long.toString(user.getId()))
                        .param("festivalId", Long.toString(festival.getId()))
                        .param("rating", Integer.toString(rating))
                        .param("comment", comment))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddAccommodationReview_thenReturnReview() throws Exception {
        when(reviewService.addAccommodationReview(user.getId(), accommodation.getId(), rating, comment))
                .thenReturn(accommodationReview);

        mockMvc.perform(post("/review/add/accommodation")
                        .param("userId", Long.toString(user.getId()))
                        .param("accommodationId", Long.toString(accommodation.getId()))
                        .param("rating", Integer.toString(rating))
                        .param("comment", comment))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(user.getId()))
                .andExpect(jsonPath("$.accommodation.id").value(accommodation.getId()))
                .andExpect(jsonPath("$.rating").value(rating))
                .andExpect(jsonPath("$.comment").value(comment));
    }

    @Test
    void whenAddAccommodationReview_thenThrowEntityNotFoundException() throws Exception {
        when(reviewService.addAccommodationReview(user.getId(), festival.getId(), rating, comment))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/review/add/accommodation")
                        .param("userId", Long.toString(user.getId()))
                        .param("accommodationId", Long.toString(accommodation.getId()))
                        .param("rating", Integer.toString(rating))
                        .param("comment", comment))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetReviewByFestivalId_thenReturnReview() throws Exception {
        when(reviewService.findAllByFestivalId(festival.getId())).thenReturn(List.of(festivalReview));

        mockMvc.perform(get("/review/festival/{festivalId}", festival.getId()))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].festival.id").value(festival.getId()))
                .andExpect(jsonPath("$[0].rating").value(rating))
                .andExpect(jsonPath("$[0].comment").value(comment));
    }

    @Test
    void givenNonExistentFestivalId_whenGetReviewByFestivalId_thenThrowEntityNotFoundException() throws Exception {
        when(reviewService.findAllByFestivalId(festival.getId())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/review/festival/{festivalId}", festival.getId()))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    

}