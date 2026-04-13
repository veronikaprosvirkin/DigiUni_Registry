package university;

import java.util.ArrayList;
import java.util.List;
import faculty.Faculty;

public class University {
    private List<Faculty> faculties = new ArrayList<>();
    private final UniversityInfo info;

    public University() {
        this(new UniversityInfo(
                "National University Kyiv Mohyla Academy",
                "NaUKMA",
                "Kyiv",
                "2 Hryhoriya Skovorody Str"
        ));
    }

    public University(UniversityInfo info) {
        this.info = info;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public UniversityInfo getInfo() {
        return info;
    }

    public String getUniversityFullName() {
        return info.fullName();
    }

    public String getUniversityShortName() {
        return info.shortName();
    }

    public String getUniversityCity() {
        return info.city();
    }

    public String getUniversityAddress() {
        return info.address();
    }
}