package faculty;

import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import university.University;
import user.UserService;
import utils.IdGenerator;
import utils.FileStorageUtils;
import person.Teacher;

public class FacultyService {
    private static final Logger log = LoggerFactory.getLogger(FacultyService.class);
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

    public void addNewFaculty(String name, String shortName, String contact, Teacher dean, UserService userService) {
        if (isNameDuplicate(university.getFaculties(), name, null, Faculty::getName)) {
            log.warn("Failed to add faculty '{}': duplicate name", name);
            System.out.println("Error: Faculty with name '" + name + "' already exists.");
            return;
        }
        Faculty faculty = new Faculty(IdGenerator.generateFacultyId(),name, shortName, contact, dean);
        university.getFaculties().add(faculty);
        FileStorageUtils.saveAll(university, userService);
        log.info("Faculty {} created", faculty.getId());
        System.out.println("Faculty added successfully.");
    }

    public void deleteFaculty(Faculty selectedFacultyToDelete, UserService userService) {
        if (university.getFaculties().remove(selectedFacultyToDelete)) {
            FileStorageUtils.saveAll(university, userService);
            log.info("Faculty {} deleted", selectedFacultyToDelete.getId());
        } else if (selectedFacultyToDelete != null) {
            log.warn("Failed to delete faculty {}: not found", selectedFacultyToDelete.getId());
        }
    }

    public void editFacultyName(Faculty faculty, String newName, UserService userService) {
        if (isNameDuplicate(university.getFaculties(), newName, faculty, Faculty::getName)) {
            log.warn("Failed to rename faculty {} to '{}': duplicate name", faculty.getId(), newName);
            System.out.println("Error: Faculty with name '" + newName + "' already exists.");
            return;
        }
        String oldName = faculty.getName();
        faculty.setName(newName);
        FileStorageUtils.saveAll(university,userService );
        log.info("Faculty {} renamed from '{}' to '{}'", faculty.getId(), oldName, newName);
        System.out.println(oldName+" name updated successfully to: " + faculty.getName());
    }

    public void assignDean(Faculty faculty, Teacher dean, UserService userService) {
        if (faculty == null || dean == null) {
            log.warn("Failed to assign dean: faculty or dean is null");
            return;
        }
        faculty.setDean(dean);
        FileStorageUtils.saveAll(university, userService);
        log.info("Dean {} assigned to faculty {}", dean.getId(), faculty.getId());
    }

    public Faculty findById(String id){
        return university.getFaculties().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst().orElse(null);
    }
}
