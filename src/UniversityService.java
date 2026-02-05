import java.util.ArrayList;
import java.util.List;

public class UniversityService {
    private University university;

    public UniversityService() {
        this.university = new University();
        initializeStructure();
    }

    // Creating base structure: Faculty-Speciality-Department
    private void initializeStructure() {
        Faculty fi = new Faculty("Faculty of Informatics");

        //Specialities
        Speciality se = new Speciality("Software Engineering");
        Speciality cs = new Speciality("Computer Science");
        Speciality acitr = new Speciality("Automation, Computer-Integrated Technologies, and Robotics");
        Speciality ap = new Speciality("Applied Mathematics");
        Speciality sa = new Speciality("Systems Analysis");

        //Departments
        Department dep_cs = new Department("Department of Computer Science");
        Department dep_ms = new Department("Department of Multimedia Systems");
        Department dep_acitr = new Department("Department of Automation, Computer-Integrated Technologies, and Robotics");
        Department dep_math = new Department("Department of Mathematics"); // AM and SA are here

        //Specialities of Faculty FI
        fi.getSpeciality().add(se);
        fi.getSpeciality().add(cs);
        fi.getSpeciality().add(acitr);
        fi.getSpeciality().add(ap);
        fi.getSpeciality().add(sa);

        //Departments of Faculty FI
        fi.getDepartments().add(dep_cs);
        fi.getDepartments().add(dep_ms);
        fi.getDepartments().add(dep_acitr);
        fi.getDepartments().add(dep_math);


        university.getFaculties().add(fi);

        // === FACULTY OF ECONOMICS (FE) ===
        Faculty fen = new Faculty("Faculty of Economics");

        //Specialities
        Speciality ma = new Speciality("Marketing");
        Speciality econ = new Speciality("Economics");
        Speciality fin = new Speciality("Finance, Banking and Insurance");
        Speciality mng = new Speciality("Management");

        //Departments
        Department dep_et = new Department("Department of Economic Theory");
        Department dep_fin = new Department("Department of Finance");
        Department dep_mbm = new Department("Department of Marketing and Business Management");

        //Specialities of Faculty FE
        fen.getSpeciality().add(ma);
        fen.getSpeciality().add(econ);
        fen.getSpeciality().add(fin);
        fen.getSpeciality().add(mng);

        //Departments of Faculty FE
        fen.getDepartments().add(dep_et);
        fen.getDepartments().add(dep_fin);
        fen.getDepartments().add(dep_mbm);

        university.getFaculties().add(fen);

        // === FACULTY OF HUMANITIES (FH) ===
        Faculty fh = new Faculty("Faculty of Humanities");

        //Specialities
        Speciality hist = new Speciality("History and Archaeology");
        Speciality phil = new Speciality("Philosophy");
        Speciality cult = new Speciality("Cultural Studies");
        Speciality ling = new Speciality("Philology");

        //Departments
        Department dep_hist = new Department("Department of History");
        Department dep_arch = new Department("Department of Archaeology");
        Department dep_phil = new Department("Department of Philosophy and Religious Studies");
        Department dep_cult = new Department("Department of Cultural Studies");
        Department dep_lit = new Department("Department of Literature");
        Department dep_engl = new Department("Department of English Language");

        //Specialities of Faculty FH
        fh.getSpeciality().add(hist);
        fh.getSpeciality().add(phil);
        fh.getSpeciality().add(cult);
        fh.getSpeciality().add(ling);

        //Departments of Faculty FH
        fh.getDepartments().add(dep_hist);
        fh.getDepartments().add(dep_arch);
        fh.getDepartments().add(dep_phil);
        fh.getDepartments().add(dep_cult);
        fh.getDepartments().add(dep_lit);
        fh.getDepartments().add(dep_engl);

        university.getFaculties().add(fh);

        // === FACULTY OF LAW (FL) ===
        Faculty fl = new Faculty("Faculty of Law");

        //Specialities
        Speciality law = new Speciality("Law");
        Speciality pma = new Speciality("Public Management and Administration");

        //Departments
        Department dep_gjd = new Department("Department of General Juridical Disciplines");
        Department dep_iel = new Department("Department of International and European Law");
        Department dep_pl = new Department("Department of Public Law");
        Department dep_prl = new Department("Department of Private Law");

        //Specialities of Faculty FL
        fl.getSpeciality().add(law);
        fl.getSpeciality().add(pma);

        //Departments of Faculty FL
        fl.getDepartments().add(dep_gjd);
        fl.getDepartments().add(dep_iel);
        fl.getDepartments().add(dep_pl);
        fl.getDepartments().add(dep_prl);

        university.getFaculties().add(fl);

        // === FACULTY OF NATURAL SCIENCES (FNS) ===
        Faculty fns = new Faculty("Faculty of Natural Sciences");

        //Specialities
        Speciality bio = new Speciality("Biology and Biotechnology");
        Speciality eco = new Speciality("Ecology");
        Speciality chem = new Speciality("Chemistry");
        Speciality phys = new Speciality("Physics and Astronomy");

        //Departments
        Department dep_bio = new Department("Department of Biology");
        Department dep_eco = new Department("Department of Environmental Studies");
        Department dep_chem = new Department("Department of Chemistry");
        Department dep_pms = new Department("Department of Physical and Mathematical Sciences");

        //Specialities of Faculty FNS
        fns.getSpeciality().add(bio);
        fns.getSpeciality().add(eco);
        fns.getSpeciality().add(chem);
        fns.getSpeciality().add(phys);

        //Departments of Faculty FNS
        fns.getDepartments().add(dep_bio);
        fns.getDepartments().add(dep_eco);
        fns.getDepartments().add(dep_chem);
        fns.getDepartments().add(dep_pms);

        university.getFaculties().add(fns);

        // === FACULTY OF SOCIAL SCIENCES AND SOCIAL TECHNOLOGIES (FSSST) ===
        Faculty fssst = new Faculty("Faculty of Social Sciences and Social Technologies");

        //Specialities
        Speciality soc = new Speciality("Sociology");
        Speciality pol = new Speciality("Political Science");
        Speciality psy = new Speciality("Psychology");
        Speciality sw = new Speciality("Social Work");
        Speciality jour = new Speciality("Journalism");
        Speciality ir = new Speciality("International Relations");

        //Departments
        Department dep_soc = new Department("Department of Sociology");
        Department dep_pol = new Department("Department of Political Science");
        Department dep_psy = new Department("Department of Psychology and Pedagogy");
        Department school_sw = new Department("School of Social Work");
        Department school_jour = new Department("Mohyla School of Journalism");
        Department school_ph = new Department("School of Public Health");

        //Specialities of Faculty FSSST
        fssst.getSpeciality().add(soc);
        fssst.getSpeciality().add(pol);
        fssst.getSpeciality().add(psy);
        fssst.getSpeciality().add(sw);
        fssst.getSpeciality().add(jour);
        fssst.getSpeciality().add(ir);

        //Departments of Faculty FSSST
        fssst.getDepartments().add(dep_soc);
        fssst.getDepartments().add(dep_pol);
        fssst.getDepartments().add(dep_psy);
        fssst.getDepartments().add(school_sw);
        fssst.getDepartments().add(school_jour);
        fssst.getDepartments().add(school_ph);

        university.getFaculties().add(fssst);
    }

    public List<Faculty> getFaculties() {
        return university.getFaculties();
    }


    /** * =====   WORK WITH STUDENTS  ===== * **/
    public void addStudent(String name, String surname, int course, int group) {
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getSpeciality().isEmpty()) {

            Faculty defaultFaculty = university.getFaculties().get(0);
            Speciality defaultSpec = defaultFaculty.getSpeciality().get(0);

            Student newStudent = new Student(name,surname, course, group,
                    defaultFaculty.getName(),
                    defaultSpec.getName());

            defaultSpec.getStudents().add(newStudent);
        } else {
            System.out.println("Error: No department found to add student!");
        }
    }

    public void addStudentToDepartment(Student student, Speciality speciality) {
        speciality.getStudents().add(student);
    }

    /** ===== SEARCH ===== **/
    // search all students
    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                allStudents.addAll(spec.getStudents());
            }
        }
        if (allStudents.isEmpty()) {
            System.out.println("No students found!");
        }
        return allStudents;
    }

    // Search by name
    public List<Student> findStudentsByFullName(String namePart) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Student s : spec.getStudents()) {
                    if (s.getFullName().toLowerCase().contains(namePart.toLowerCase())) {
                        result.add(s);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            System.out.println("No student found by full name " + namePart);
        }
        return result;
    }

    // Search by surname
    public List<Student> findStudentsBySurname(String surname) {
        List<Student> result = new ArrayList<>();
        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Student s : spec.getStudents()) {
                    if (s.getSurname().equalsIgnoreCase(surname)) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    // Search by group
    public List<Student> findStudentsByGroup(int group) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Student s : spec.getStudents()) {
                    if (s.getGroup() == group) {
                        result.add(s);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            System.out.println("No student found in group " + group);
        }
        return result;
    }

    // Search by Course
    public List<Student> findStudentsByCourse(int course) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Student s : spec.getStudents()) {
                    if (s.getCourse() == course) {
                        result.add(s);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            System.out.println("No student found on course " + course);
        }
        return result;
    }

    /** * =====   WORK WITH TEACHERS  ===== * **/
    // Adding a teacher
    public void addTeacher(String name, String surname, String position) {
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getDepartments().isEmpty()) {
            // Deciding where to sign (first Faculty and first Department)
            university.getFaculties().get(0)
                    .getDepartments().get(0)
                    .getTeachers().add(new Teacher(name, surname, position));
        }
    }

    /** ===== SEARCH ===== **/
    // Find teachers by name
    public List<Teacher> findTeachersByFullName(String namePart) {
        List<Teacher> result = new ArrayList<>();

        for (Faculty f : university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Teacher t : d.getTeachers()) {
                    if (t.getFullName().toLowerCase().contains(namePart.toLowerCase())) {
                        result.add(t);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            System.out.println("No teacher found by name " + namePart);
        }
        return result;
    }
}