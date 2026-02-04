import java.util.ArrayList;
import java.util.List;

public class University {
    private List<Faculty> faculties = new ArrayList<>();
    private final String universityFullName = "National University Kyiv Mohyla Academy";
    private final String universityShortName = "NaUKMA";
    private final String universityCity = "Kyiv";
    private final String universityAddress = "2 Hryhoriya Skovorody Str";

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }
}