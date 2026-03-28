import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Speciality implements NamedEntity {
    private String nameOfSpeciality;
    private List<Group> groups = new ArrayList<>();
    private String id;


    public List<Group> getGroups() {
        return groups;
    }
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Speciality(String id,String nameOfSpeciality) {
        this.nameOfSpeciality = nameOfSpeciality;
        this.id = id;
    }
    public String getId() {
        return id;
    }


    public String getName() { return nameOfSpeciality; }
    public void setName(String name) { this.nameOfSpeciality = name; }

    @Override
    public String toString() {
        return nameOfSpeciality;
    }

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfSpeciality;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Speciality that = (Speciality) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}