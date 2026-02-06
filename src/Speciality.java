import java.util.ArrayList;
import java.util.List;

public class Speciality {
    private String nameOfSpeciality;
    private List<Group> groups = new ArrayList<>();

    public List<Group> getGroups() {
        return groups;
    }
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Speciality(String nameOfSpeciality) {
        this.nameOfSpeciality = nameOfSpeciality;
    }


    public String getName() { return nameOfSpeciality; }
}