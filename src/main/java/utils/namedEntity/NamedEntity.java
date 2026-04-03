package utils.namedEntity;

public interface NamedEntity {
    String getName();
    void setName(String newName);

    default String getDisplayInfo(){
        return "";
    }

    default void printInfo() {
        System.out.println("Entity Name: " + getName());
    }
}
