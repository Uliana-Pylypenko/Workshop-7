package pl.coderslab.workshop7.festival;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/festival")
public class FestivalController {
    private final FestivalService festivalService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Festival>> getUpcomingFestivalsByCategory(@PathVariable int categoryId) {
        try {
            FestivalCategory category = FestivalCategory.getById(categoryId);
            return ResponseEntity.ok(festivalService.getUpcomingFestivalsByCategory(category));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
        try {
            List<Festival> festivals = festivalService.findAllByStartDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
            return ResponseEntity.ok(festivals);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Festival> getFestivalById(@PathVariable Long id) {
        return new ResponseEntity<>(festivalService.getFestivalById(id), HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Festival>> getFestivalByPriceRange(@RequestParam Double lower, @RequestParam Double higher) {
        try {
            List<Festival> festivals = festivalService.findAllByPricePerDayBetween(lower, higher);
            return ResponseEntity.ok(festivals);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
