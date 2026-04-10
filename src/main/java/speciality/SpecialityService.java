package speciality;

import java.util.Collection;
import java.util.Objects;
import university.University;
import faculty.Faculty;
import utils.IdGenerator;
import utils.FileStorageUtils;

public class SpecialityService {
    private University university;

    public SpecialityService(University university) {
        this.university = university;
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, T entityToExclude, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .filter(item -> item != entityToExclude)
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void addNewSpeciality(String newSpecialityName, Faculty selectedFaculty) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(newSpecialityName, "Speciality name cannot be null");
        boolean exists = selectedFaculty.getSpeciality().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newSpecialityName));

        if (exists) {
            System.out.println("Error: Speciality with this name already exists!");
            return;
        }
        selectedFaculty.getSpeciality().add(new Speciality(IdGenerator.generateSpecialityId(),newSpecialityName));
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println("Speciality created successfully!");
    }

    public void editSpecialityName(Speciality speciality, String editName, Faculty faculty) {
        Objects.requireNonNull(speciality, "Speciality cannot be null");
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        Objects.requireNonNull(editName, "New name cannot be null");
        if (isNameDuplicate(faculty.getSpeciality(), editName, speciality, Speciality::getName)) {
            System.out.println("Error: Speciality with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = speciality.getName();
        speciality.setName(editName);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println(oldName+" speciality name updated successfully to: " + speciality.getName());
    }


    public void deleteSpeciality(Speciality selectedSpeciality, Faculty selectedFaculty) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(selectedSpeciality, "Speciality cannot be null");
        if (selectedFaculty.getSpeciality().remove(selectedSpeciality)) {
            FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        }
    }

    public Speciality findById(String id) {
        for (Faculty faculty : university.getFaculties()){
            if (faculty.getSpeciality()!= null ){
                Speciality found = faculty.getSpeciality().stream()
                        .filter(s -> s.getId().equals(id)).findFirst().orElse(null);
                if (found != null) return found;
            }
        }
        return null;
    }

}
