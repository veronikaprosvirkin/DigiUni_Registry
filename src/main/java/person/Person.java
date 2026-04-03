package person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.namedEntity.NamedEntity;

// Base Person entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract sealed class Person implements NamedEntity permits Student, Teacher {
    @EqualsAndHashCode.Include
    protected String id;
    protected String name;
    protected String surname;
    protected String patronymic;
    protected String email;
    protected String phone;

    public Person(String id, String name, String surname, String patronymic) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    @Override
    public String getName() {
        return this.surname + " " + this.name;
    }

    // Get only name
    public String getOnlyName() {
        return this.name;
    }

    // Get full name
    public String getFullName() {
        return getName();
    }

    public abstract String getDisplayInfo();
}