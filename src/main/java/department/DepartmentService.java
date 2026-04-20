package department;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import university.University;
import university.UniversityService;
import user.UserService;
import utils.IdGenerator;
import utils.FileStorageUtils;
import person.Teacher;
import faculty.Faculty;

public class DepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private University university;

    public DepartmentService(University university) {
        this.university = university;
    }
    public List<Department> getDepartments(Faculty faculty) {
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        return faculty.getDepartments();
    }

    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty, UserService userService) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        addNewDepartment(newDepartmentName, selectedFaculty, null, null, userService);
    }
    
    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty, Teacher head, String location, UserService userService) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(newDepartmentName, "Department name cannot be null");
        boolean exists = selectedFaculty.getDepartments().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newDepartmentName));

        if (exists) {
            log.warn("Failed to add department '{}': duplicate in faculty {}", newDepartmentName, selectedFaculty.getId());
            System.out.println("Error: Department with this name already exists!");
            return;
        }
        Department d = new Department(IdGenerator.generateDepartmentId(),newDepartmentName);
        d.setFaculty(selectedFaculty);
        if (head != null) {
            d.setHead(head);
            head.setDepartment(d);
            head.setFaculty(selectedFaculty);
        }
        if (location != null && !location.trim().isEmpty()) {
            d.setLocation(location);
        }
        selectedFaculty.getDepartments().add(d);
        FileStorageUtils.saveAll(university, userService);
        log.info("Department {} created in faculty {}", d.getId(), selectedFaculty.getId());
        System.out.println("Department created successfully!");
    }

    public void editDepartmentName(Department dept, String editName, Faculty faculty, UserService userService) {
        Objects.requireNonNull(dept, "Department cannot be null");
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        Objects.requireNonNull(editName, "New name cannot be null");
        if (isNameDuplicate(faculty.getDepartments(), editName, dept, Department::getName)) {
            log.warn("Failed to rename department {} to '{}': duplicate in faculty {}", dept.getId(), editName, faculty.getId());
            System.out.println("Error: Department with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = dept.getName();
        dept.setName(editName);
        FileStorageUtils.saveAll(university, userService);
        log.info("Department {} renamed from '{}' to '{}'", dept.getId(), oldName, editName);
        System.out.println(oldName+" name updated successfully to: " + dept.getName());
    }

    public void editDepartmentHead(Department dept, Teacher head, UserService userService) {
        Objects.requireNonNull(dept, "Department cannot be null");
        dept.setHead(head);
        if (head != null) {
            head.setDepartment(dept);
            head.setFaculty(dept.getFaculty());
        }
        FileStorageUtils.saveAll(university, userService);
        log.info("Department {} head set to {}", dept.getId(), head == null ? "None" : head.getId());
        System.out.println("Head of department set to " + (head == null ? "None" : head.getDisplayInfo()));
    }

    public void editDepartmentLocation(Department dept, String location, UserService userService) {
        Objects.requireNonNull(dept, "Department cannot be null");
        dept.setLocation(location);
        FileStorageUtils.saveAll(university, userService);
        log.info("Department {} location updated", dept.getId());
        System.out.println("Location updated.");
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, T entityToExclude, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .filter(item -> item != entityToExclude)
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void deleteDepartment(Department selectedDept, Faculty selectedFaculty, UserService userService) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(selectedDept, "Department cannot be null");
        if (selectedFaculty.getDepartments().remove(selectedDept)) {
            FileStorageUtils.saveAll(university, userService);
            log.info("Department {} deleted from faculty {}", selectedDept.getId(), selectedFaculty.getId());
        } else {
            log.warn("Failed to delete department {} from faculty {}: not found", selectedDept.getId(), selectedFaculty.getId());
        }
    }
}
