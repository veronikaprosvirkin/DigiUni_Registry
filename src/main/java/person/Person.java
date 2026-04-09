package person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import utils.namedEntity.NamedEntity;
import java.time.LocalDate;
import java.time.Period;

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
    protected LocalDate dateOfBirth;

    @Setter(AccessLevel.NONE)
    protected Integer age;

    public Person(String id, String name, String surname, String patronymic) {
        this(id, name, surname, patronymic, null);
    }

    public Person(String id, String name, String surname, String patronymic, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        setDateOfBirth(dateOfBirth);
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        if (dateOfBirth != null) {
            this.age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        } else {
            this.age = null;
        }
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