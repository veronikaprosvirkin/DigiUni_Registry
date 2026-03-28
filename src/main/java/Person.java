import java.util.Objects;

public class Person implements NamedEntity {
    private String name;
    private String surname;
    private String id;


    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * Builds the fullName of Person: surname + name
     * @return full person's name
     */
    @Override
    public String getName() {
        return this.surname+" "+ this.name;
    }
    public String getSurname() {return surname;}
    public String getOnlyName() {return this.name;}
    public String getFullName() {return this.surname+" "+ this.name;}

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
