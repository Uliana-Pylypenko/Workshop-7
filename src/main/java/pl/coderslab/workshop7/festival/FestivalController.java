package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/festival")
public class FestivalController {
    private final FestivalService festivalService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Festival>> getUpcomingFestivalsByCategory(@PathVariable int categoryId) {
        FestivalCategory category = FestivalCategory.getById(categoryId);
        return ResponseEntity.ok(festivalService.getUpcomingFestivalsByCategory(category));
    }

}
