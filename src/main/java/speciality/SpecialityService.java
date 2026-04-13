package speciality;

import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import university.University;
import faculty.Faculty;
import user.UserService;
import utils.IdGenerator;
import utils.FileStorageUtils;

public class SpecialityService {
    private static final Logger log = LoggerFactory.getLogger(SpecialityService.class);
    private University university;

    public SpecialityService(University university) {
        this.university = university;
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, T entityToExclude, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .filter(item -> item != entityToExclude)
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void addNewSpeciality(String newSpecialityName, Faculty selectedFaculty, UserService userService) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(newSpecialityName, "Speciality name cannot be null");
        boolean exists = selectedFaculty.getSpeciality().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newSpecialityName));

        if (exists) {
            log.warn("Failed to add speciality '{}': duplicate in faculty {}", newSpecialityName, selectedFaculty.getId());
            System.out.println("Error: Speciality with this name already exists!");
            return;
        }
        Speciality speciality = new Speciality(IdGenerator.generateSpecialityId(),newSpecialityName);
        selectedFaculty.getSpeciality().add(speciality);
        FileStorageUtils.saveAll(university, userService);
        log.info("Speciality {} created in faculty {}", speciality.getId(), selectedFaculty.getId());
        System.out.println("Speciality created successfully!");
    }

    public void editSpecialityName(Speciality speciality, String editName, Faculty faculty, UserService userService) {
        Objects.requireNonNull(speciality, "Speciality cannot be null");
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        Objects.requireNonNull(editName, "New name cannot be null");
        if (isNameDuplicate(faculty.getSpeciality(), editName, speciality, Speciality::getName)) {
            log.warn("Failed to rename speciality {} to '{}': duplicate in faculty {}", speciality.getId(), editName, faculty.getId());
            System.out.println("Error: Speciality with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = speciality.getName();
        speciality.setName(editName);
        FileStorageUtils.saveAll(university, userService);
        log.info("Speciality {} renamed from '{}' to '{}'", speciality.getId(), oldName, editName);
        System.out.println(oldName+" speciality name updated successfully to: " + speciality.getName());
    }


    public void deleteSpeciality(Speciality selectedSpeciality, Faculty selectedFaculty, UserService userService) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(selectedSpeciality, "Speciality cannot be null");
        if (selectedFaculty.getSpeciality().remove(selectedSpeciality)) {
            FileStorageUtils.saveAll(university, userService);
            log.info("Speciality {} deleted from faculty {}", selectedSpeciality.getId(), selectedFaculty.getId());
        } else {
            log.warn("Failed to delete speciality {} from faculty {}: not found", selectedSpeciality.getId(), selectedFaculty.getId());
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
        log.info("Speciality not found by id {}", id);
        return null;
    }

}
