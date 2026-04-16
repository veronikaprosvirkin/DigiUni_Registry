package person;

import java.util.Set;

public final class GenderInferenceUtils {
    private static final Set<String> MALE_NAMES_ENDING_WITH_A = Set.of(
            "mykola", "illya", "ilia", "luka", "kuzma", "sava", "nikita"
    );

    private GenderInferenceUtils() {
    }

    public static Gender infer(String name, String patronymic) {
        Gender fromPatronymic = inferFromPatronymic(patronymic);
        if (fromPatronymic != Gender.PREFER_NOT_TO_SAY) {
            return fromPatronymic;
        }

        Gender fromName = inferFromName(name);
        if (fromName != Gender.PREFER_NOT_TO_SAY) {
            return fromName;
        }

        return Gender.PREFER_NOT_TO_SAY;
    }

    private static Gender inferFromPatronymic(String patronymic) {
        if (patronymic == null || patronymic.isBlank()) {
            return Gender.PREFER_NOT_TO_SAY;
        }

        String normalized = patronymic.trim().toLowerCase();

        if (normalized.endsWith("vna") || normalized.endsWith("ivna") || normalized.endsWith("yivna")) {
            return Gender.FEMALE;
        }
        if (normalized.endsWith("ych") || normalized.endsWith("ich") || normalized.endsWith("ovych") || normalized.endsWith("evych")) {
            return Gender.MALE;
        }

        return Gender.PREFER_NOT_TO_SAY;
    }

    private static Gender inferFromName(String name) {
        if (name == null || name.isBlank()) {
            return Gender.PREFER_NOT_TO_SAY;
        }

        String normalized = name.trim().toLowerCase();
        if (MALE_NAMES_ENDING_WITH_A.contains(normalized)) {
            return Gender.MALE;
        }

        if (normalized.endsWith("a") || normalized.endsWith("ia") || normalized.endsWith("iya")) {
            return Gender.FEMALE;
        }

        return Gender.MALE;
    }
}

