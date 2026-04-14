package speciality;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;
import utils.namedEntity.NamedEntity;
import speciality.Group;

// Speciality entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Speciality implements NamedEntity {
    @EqualsAndHashCode.Include
    private String id;
    private String nameOfSpeciality;
    private List<Group> groups = new ArrayList<>();

    public Speciality(String id, String nameOfSpeciality) {
        this.id = id;
        this.nameOfSpeciality = nameOfSpeciality;
    }

    @Override
    public String getName() {
        return nameOfSpeciality;
    }

    // Override for NamedEntity logic
    public void setName(String name) {
        this.nameOfSpeciality = name;
    }

    @Override
    public String toString() {
        return nameOfSpeciality;
    }

    public String getId() {return id;}

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfSpeciality;
    }

}