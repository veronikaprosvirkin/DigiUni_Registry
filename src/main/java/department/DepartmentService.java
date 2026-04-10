package department;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import university.University;
import utils.IdGenerator;
import utils.FileStorageUtils;
import person.Teacher;
import faculty.Faculty;

public class DepartmentService {
    private University university;

    public DepartmentService(University university) {
        this.university = university;
    }
    public List<Department> getDepartments(Faculty faculty) {
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        return faculty.getDepartments();
    }

    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        addNewDepartment(newDepartmentName, selectedFaculty, null, null);
    }
    
    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty, Teacher head, String location) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(newDepartmentName, "Department name cannot be null");
        boolean exists = selectedFaculty.getDepartments().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newDepartmentName));

        if (exists) {
            System.out.println("Error: Department with this name already exists!");
            return;
        }
        Department d = new Department(IdGenerator.generateDepartmentId(),newDepartmentName);
        if (head != null) {
            d.setHead(head);
        }
        if (location != null && !location.trim().isEmpty()) {
            d.setLocation(location);
        }
        selectedFaculty.getDepartments().add(d);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println("Department created successfully!");
    }

    public void editDepartmentName(Department dept, String editName, Faculty faculty) {
        Objects.requireNonNull(dept, "Department cannot be null");
        Objects.requireNonNull(faculty, "Faculty cannot be null");
        Objects.requireNonNull(editName, "New name cannot be null");
        if (isNameDuplicate(faculty.getDepartments(), editName, dept, Department::getName)) {
            System.out.println("Error: Department with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = dept.getName();
        dept.setName(editName);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println(oldName+" name updated successfully to: " + dept.getName());
    }

    public void editDepartmentHead(Department dept, Teacher head) {
        Objects.requireNonNull(dept, "Department cannot be null");
        dept.setHead(head);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println("Head of department set to " + (head == null ? "None" : head.getDisplayInfo()));
    }

    public void editDepartmentLocation(Department dept, String location) {
        Objects.requireNonNull(dept, "Department cannot be null");
        dept.setLocation(location);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        System.out.println("Location updated.");
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, T entityToExclude, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .filter(item -> item != entityToExclude)
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void deleteDepartment(Department selectedDept, Faculty selectedFaculty) {
        Objects.requireNonNull(selectedFaculty, "Faculty cannot be null");
        Objects.requireNonNull(selectedDept, "Department cannot be null");
        if (selectedFaculty.getDepartments().remove(selectedDept)) {
            FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        }
    }
}
