public sealed interface NamedEntity permits Person, Department, Faculty, Speciality {
    String getName();
    void setName(String newName);

    default String getDisplayInfo(){
        return "";
    }

    default void printInfo() {
        System.out.println("Entity Name: " + getName());
    }
}
