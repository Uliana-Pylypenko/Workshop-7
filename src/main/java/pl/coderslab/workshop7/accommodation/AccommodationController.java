package pl.coderslab.workshop7.accommodation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accommodation")
public class AccommodationController {
    private final AccommodationService service;

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Accommodation>> getAccommodationsByLocation(@PathVariable String location) {
        return new ResponseEntity<>(service.findByLocation(location), HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Accommodation>> getAccommodationByPriceRange(@RequestParam double low, @RequestParam double high) {
        return new ResponseEntity<>(service.findByPricePerDayBetween(low, high), HttpStatus.OK);
    }

    @GetMapping("/festival/{id}")
    public ResponseEntity<List<Accommodation>> getAccommodationByFestivalId(@PathVariable Long id) {
        return new ResponseEntity<>(service.findByFestivalId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accommodation> getAccommodationById(@PathVariable Long id) {
        Accommodation accommodation = service.findById(id);
        return new ResponseEntity<>(accommodation, HttpStatus.OK);
    }
}
