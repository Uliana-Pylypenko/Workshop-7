package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
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

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Festival>> getFestivalsByName(@PathVariable String name) {
        return ResponseEntity.ok(festivalService.findAllByNameContainingIgnoreCase(name));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Festival>> getFestivalsByLocation(@PathVariable String location) {
        return ResponseEntity.ok(festivalService.findAllByLocationContainingIgnoreCase(location));
    }

    @GetMapping("/start-date")
    public ResponseEntity<List<Festival>> getFestivalByStartDate(@RequestParam String startDate, @RequestParam String endDate) {
        List<Festival> festivals = festivalService.findAllByStartDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(festivals);
//        try {
//            List<Festival> festivals = festivalService.findAllByStartDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
//            return ResponseEntity.ok(festivals);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().build();
//        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Festival> getFestivalById(@PathVariable Long id) {
        return new ResponseEntity<>(festivalService.getDetailsById(id), HttpStatus.OK);
    }
}
