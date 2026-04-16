package person;

public enum Position {
    DEAN("Dean"),
    DEPUTY_DEAN("Deputy Dean"),
    HEAD_OF_DEPARTMENT("Head of Department"),

    // === АКАДЕМІЧНІ ПОСАДИ ===
    PROFESSOR("Professor"),
    ASSOCIATE_PROFESSOR("Associate Professor"),
    SENIOR_LECTURER("Senior Lecturer"),
    LECTURER("Lecturer"),
    ASSISTANT("Assistant"),

    // === ДОДАТКОВІ (ОПЦІОНАЛЬНІ) ===
    GUEST_LECTURER("Guest Lecturer"),
    RESEARCHER("Researcher");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Position fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        for (Position p : Position.values()) {
            if (p.displayName.equalsIgnoreCase(text.trim()) || p.name().equalsIgnoreCase(text.trim())) {
                return p;
            }
        }
        return null;
    }
}