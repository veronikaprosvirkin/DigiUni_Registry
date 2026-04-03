package department;

import java.util.Collection;
import java.util.List;
import university.University;
import utils.IdGenerator;
import person.Teacher;
import faculty.Faculty;

public class DepartmentService {
    private University university;

    public DepartmentService(University university) {
        this.university = university;
    }
    public List<Department> getDepartments(Faculty faculty) {
        return faculty.getDepartments();
    }

    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty) {
        addNewDepartment(newDepartmentName, selectedFaculty, null, null);
    }
    
    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty, Teacher head, String location) {
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
        System.out.println("Department created successfully!");
    }

    public void editDepartmentName(Department dept, String editName, Faculty faculty) {
        if (isNameDuplicate(faculty.getDepartments(), editName, Department::getName)) {
            System.out.println("Error: Department with name '" + editName + "' already exists on this faculty.");
            return;
        }
        String oldName = dept.getName();
        dept.setName(editName);
        System.out.println(oldName+" name updated successfully to: " + dept.getName());
    }

    public void editDepartmentHead(Department dept, Teacher head) {
        dept.setHead(head);
        System.out.println("Head of department set to " + (head == null ? "None" : head.getDisplayInfo()));
    }

    public void editDepartmentLocation(Department dept, String location) {
        dept.setLocation(location);
        System.out.println("Location updated.");
    }

    private <T> boolean isNameDuplicate(Collection<T> list, String newName, java.util.function.Function<T, String> nameGetter) {
        return list.stream()
                .anyMatch(item -> nameGetter.apply(item).equalsIgnoreCase(newName));
    }

    public void deleteDepartment(Department selectedDept, Faculty selectedFaculty) {
        selectedFaculty.getDepartments().remove(selectedDept);
    }
}
