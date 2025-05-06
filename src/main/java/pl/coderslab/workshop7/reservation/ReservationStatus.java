package pl.coderslab.workshop7.reservation;


import java.util.stream.Stream;

public enum ReservationStatus {
    IN_PROGRESS(0),
    CONFIRMED(1),
    REJECTED(2),
    CANCELLED(3);

    private int value;

    private ReservationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReservationStatus fromInt(int value) {
        return Stream.of(ReservationStatus.values())
                .filter(r -> r.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
