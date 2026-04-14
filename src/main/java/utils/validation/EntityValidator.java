package utils.validation;

import java.lang.reflect.Field;

public final class EntityValidator {
    private EntityValidator() {
    }

    public static void validate(Object obj) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }

        Class<?> current = obj.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                field.setAccessible(true);
                validateNotNull(obj, field);
                validateMinValue(obj, field);
            }
            current = current.getSuperclass();
        }
    }

    private static void validateNotNull(Object obj, Field field) {
        NotNull notNull = field.getAnnotation(NotNull.class);
        if (notNull == null) {
            return;
        }

        try {
            Object value = field.get(obj);
            if (value == null || (value instanceof String str && str.isBlank())) {
                throw new IllegalArgumentException(notNull.message());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access field: " + field.getName(), e);
        }
    }

    private static void validateMinValue(Object obj, Field field) {
        MinValue minValue = field.getAnnotation(MinValue.class);
        if (minValue == null) {
            return;
        }

        try {
            Object value = field.get(obj);
            if (!(value instanceof Number number)) {
                throw new IllegalArgumentException("@MinValue can be used only on numeric fields: " + field.getName());
            }
            if (number.longValue() < minValue.value()) {
                throw new IllegalArgumentException(minValue.message());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access field: " + field.getName(), e);
        }
    }
}

