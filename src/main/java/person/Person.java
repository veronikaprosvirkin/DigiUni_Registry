package person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import utils.namedEntity.NamedEntity;
import utils.validation.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

// Base Person entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract sealed class Person implements NamedEntity, Serializable permits Student, Teacher {
    private static final long serialVersionUID = 1L;
    @EqualsAndHashCode.Include
    protected String id;
    @NotNull(message = "Name is required")
    protected String name;
    @NotNull(message = "Surname is required")
    protected String surname;
    protected String patronymic;
    protected Gender gender;
    private boolean genderManuallySet;
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
        this.gender = GenderInferenceUtils.infer(name, patronymic);
        this.genderManuallySet = false;
        setDateOfBirth(dateOfBirth);
    }

    public void setName(String name) {
        this.name = name;
        inferGenderIfNotManuallyOverridden();
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        inferGenderIfNotManuallyOverridden();
    }

    public void changeGender(Gender newGender) {
        if (newGender != null) {
            this.gender = newGender;
            this.genderManuallySet = true;
        }
    }

    public void setGender(Gender gender) {
        changeGender(gender);
    }

    public void inferGenderFromName() {
        this.gender = GenderInferenceUtils.infer(this.name, this.patronymic);
        this.genderManuallySet = false;
    }

    private void inferGenderIfNotManuallyOverridden() {
        if (!this.genderManuallySet) {
            this.gender = GenderInferenceUtils.infer(this.name, this.patronymic);
        }
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