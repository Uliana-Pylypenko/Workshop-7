package pl.coderslab.workshop7.festival;

public enum FestivalCategory {
    MUSIC(1),
    FILM(2),
    SCIENCE(3);

    private int id;

    FestivalCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static FestivalCategory getById(int id) {
        for (FestivalCategory category : values()) {
            if (category.getId() == id) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category with id " + id + " found");
    }


}
