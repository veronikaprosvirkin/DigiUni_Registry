package person;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Gender fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        for (Gender g : Gender.values()) {
            if (g.displayName.equalsIgnoreCase(text.trim()) || g.name().equalsIgnoreCase(text.trim())) {
                return g;
            }
        }
        return null;
    }
}