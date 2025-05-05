package pl.coderslab.workshop7.accommodation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
