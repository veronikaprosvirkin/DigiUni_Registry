package faculty;

import java.util.Collection;
import java.util.List;
import university.University;
import utils.IdGenerator;
import utils.FileStorageUtils;
import person.Teacher;

public class FacultyService {
    private University university;

    public FacultyService(University university) {
        this.university = university;
    }

    public List<Faculty> getFaculties() {
        return university.getFaculties();
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, T entityToExclude, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .filter(item -> item != entityToExclude)
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void addNewFaculty(String name, String shortName, String contact, Teacher dean) {
        if (isNameDuplicate(university.getFaculties(), name, null, Faculty::getName)) {
            System.out.println("Error: Faculty with name '" + name + "' already exists.");
            return;
        }
        university.getFaculties().add(new Faculty(IdGenerator.generateFacultyId(),name, shortName, contact, dean));
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println("Faculty added successfully.");
    }

    public void deleteFaculty(Faculty selectedFacultyToDelete) {
        if (university.getFaculties().remove(selectedFacultyToDelete)) {
            FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        }
    }

    public void editFacultyName(Faculty faculty, String newName) {
        if (isNameDuplicate(university.getFaculties(), newName, faculty, Faculty::getName)) {
            System.out.println("Error: Faculty with name '" + newName + "' already exists.");
            return;
        }
        String oldName = faculty.getName();
        faculty.setName(newName);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println(oldName+" name updated successfully to: " + faculty.getName());
    }

    public void assignDean(Faculty faculty, Teacher dean) {
        if (faculty == null || dean == null) {
            return;
        }
        faculty.setDean(dean);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
    }

    public Faculty findById(String id){
        return university.getFaculties().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst().orElse(null);
    }
}
