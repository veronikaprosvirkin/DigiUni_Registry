import java.util.Objects;

public class Person implements NamedEntity {
    private String name;
    private String surname;
    private String id;
    private String patronymic;


    public Person(String name, String surname, String patronymic) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    /**
     * Builds the fullName of Person: surname + name
     * @return full person's name
     */
    @Override
    public String getName() {
        return this.surname + " " + this.name + " " + this.patronymic;
    }
    public String getSurname() {return surname;}
    public String getOnlyName() {return this.name;}
    public String getFullName() { return this.surname + " " + this.name + " " + this.patronymic;}

    public String getPatronymic() {return patronymic;}

    public void setPatronymic(String patronymic) { this.patronymic = patronymic;}

    public void setSurname(String newSurname) {
        this.surname = newSurname;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
