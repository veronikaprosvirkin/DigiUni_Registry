
public class Person {
    private final String name;
    private final String surname;
    private final String fullName;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.fullName = surname + " " + name;
    }

    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getFullName() {return fullName;}
}
