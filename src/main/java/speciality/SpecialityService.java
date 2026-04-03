package speciality;

import java.util.Collection;
import university.University;
import faculty.Faculty;
import utils.IdGenerator;

public class SpecialityService {
    private University university;

    public SpecialityService(University university) {
        this.university = university;
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void addNewSpeciality(String newSpecialityName, Faculty selectedFaculty) {
        boolean exists = selectedFaculty.getSpeciality().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newSpecialityName));

        if (exists) {
            System.out.println("Error: Speciality with this name already exists!");
            return;
        }
        selectedFaculty.getSpeciality().add(new Speciality(IdGenerator.generateSpecialityId(),newSpecialityName));
        System.out.println("Speciality created successfully!");
    }

    public void editSpecialityName(Speciality speciality, String editName, Faculty faculty) {
        if (isNameDuplicate(faculty.getSpeciality(), editName, Speciality::getName)) {
            System.out.println("Error: Speciality with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = speciality.getName();
        speciality.setName(editName);
        System.out.println(oldName+" speciality name updated successfully to: " + speciality.getName());
    }


    public void deleteSpeciality(Speciality selectedSpeciality, Faculty selectedFaculty) {
        selectedFaculty.getSpeciality().remove(selectedSpeciality);
    }
}
