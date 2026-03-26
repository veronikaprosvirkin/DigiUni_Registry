import java.util.Collection;
import java.util.List;

public class DepartmentService {
    private University university;

    public DepartmentService(University university) {
        this.university = university;
    }
    public List<Department> getDepartments(Faculty faculty) {
        return faculty.getDepartments();
    }

    public void addNewDepartment(String newDepartmentName, Faculty selectedFaculty) {
        boolean exists = selectedFaculty.getDepartments().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(newDepartmentName));

        if (exists) {
            System.out.println("Error: Department with this name already exists!");
            return;
        }
        selectedFaculty.getDepartments().add(new Department(IdGenerator.generateDepartmentId(),newDepartmentName));
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
    private <T> boolean isNameDuplicate(Collection<T> list, String newName, java.util.function.Function<T, String> nameExtractor) {
        return list.stream()
                .anyMatch(item -> nameExtractor.apply(item).equalsIgnoreCase(newName));
    }

    public void deleteDepartment(Department selectedDept, Faculty selectedFaculty) {
        selectedFaculty.getDepartments().remove(selectedDept);
    }
}
