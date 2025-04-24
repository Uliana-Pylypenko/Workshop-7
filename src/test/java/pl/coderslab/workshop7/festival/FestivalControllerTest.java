package pl.coderslab.workshop7.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FestivalController.class)
class FestivalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FestivalService service;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getUpcomingFestivalsByCategory() throws Exception {
        Festival festival = new Festival();
        festival.setName("Festival");
        festival.setFestivalCategory(FestivalCategory.MUSIC);
        when(service.getUpcomingFestivalsByCategory(any(FestivalCategory.class))).thenReturn(List.of(festival));

        mockMvc.perform(get("/festival/category/1"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(festival.getName()))
                .andExpect(jsonPath("$[0].festivalCategory").value(festival.getFestivalCategory().toString()));

    }
}