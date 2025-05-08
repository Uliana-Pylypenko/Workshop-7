package pl.coderslab.workshop7.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.workshop7.accommodation.Accommodation;
import pl.coderslab.workshop7.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "accommodation"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Accommodation accommodation;

    private LocalDate reservationStart;
    private LocalDate reservationEnd;

//    @Enumerated(EnumType.STRING)
//    private ReservationStatus reservationStatus;

    // saves just numbers in the database
    @Basic
    private int reservationStatusValue;

    @Transient
    private ReservationStatus reservationStatus;

    @PostLoad
    void fillTransient() {
        if (reservationStatusValue > 0) {
            this.reservationStatus = ReservationStatus.fromInt(reservationStatusValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (reservationStatus != null) {
            this.reservationStatusValue = reservationStatus.getValue();
        }
    }
}
