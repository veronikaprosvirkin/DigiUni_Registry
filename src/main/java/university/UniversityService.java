package university;

import person.Teacher;
import utils.IdGenerator;
import faculty.Faculty;
import speciality.Speciality;
import department.Department;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UniversityService {
    private final University university;

    public UniversityService(University university) {
        this.university = Objects.requireNonNull(university, "university must not be null");
        initializeStructure();
    }

    // Creating base structure: Faculty-Speciality-Department
    private void initializeStructure() {
        if (!university.getFaculties().isEmpty()) {
            return; // Data already loaded
        }
        // ==========================================
        // 0. CREATING DEANS

        Teacher deanFI = new Teacher(IdGenerator.generateTeacherId(), "Andrii", "Hlybovets", "Mykolaiovych", "Dean", null);
        Teacher deanFE = new Teacher(IdGenerator.generateTeacherId(), "Oleksandra", "Humenna", "Vitaliivna", "Dean", null);
        Teacher deanFH = new Teacher(IdGenerator.generateTeacherId(), "Dmytro", "Mazin", "Mykhailovych", "Dean", null);
        Teacher deanFL = new Teacher(IdGenerator.generateTeacherId(), "Volodymyr", "Venher", "Mykolaiovych", "Dean", null);
        Teacher deanFNS = new Teacher(IdGenerator.generateTeacherId(), "Anatolii", "Bilous", "Markovych", "Dean", null);
        Teacher deanFSSST = new Teacher(IdGenerator.generateTeacherId(), "Svitlana", "Oksamytna", "Mykolaivna", "Dean", null);

        // ==========================================
        // 1. FACULTY OF INFORMATICS (FI)
        // ==========================================
        Faculty fi = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Informatics", "FI", "+38(044) 425-60-64", deanFI);

        //Specialities
        Speciality se = new Speciality(IdGenerator.generateSpecialityId(), "Software Engineering");
        Speciality cs = new Speciality(IdGenerator.generateSpecialityId(), "Computer Science");
        Speciality acitr = new Speciality(IdGenerator.generateSpecialityId(), "Automation, Computer-Integrated Technologies, and Robotics");
        Speciality ap = new Speciality(IdGenerator.generateSpecialityId(), "Applied Mathematics");
        Speciality sa = new Speciality(IdGenerator.generateSpecialityId(), "Systems Analysis");

        //Departments
        Department dep_cs = new Department(IdGenerator.generateDepartmentId(), "Department of Computer Science");
        Department dep_ms = new Department(IdGenerator.generateDepartmentId(), "Department of Multimedia Systems");
        Department dep_acitr = new Department(IdGenerator.generateDepartmentId(), "Department of Automation, Computer-Integrated Technologies, and Robotics");
        Department dep_math = new Department(IdGenerator.generateDepartmentId(), "Department of Mathematics"); // AM and SA are here

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

        // ==========================================
        // 2. FACULTY OF ECONOMICS (FE)
        // ==========================================
        Faculty fen = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Economics", "FE", "+38(044) 425-60-59", deanFE);

        //Specialities
        Speciality ma = new Speciality(IdGenerator.generateSpecialityId(), "Marketing");
        Speciality econ = new Speciality(IdGenerator.generateSpecialityId(), "Economics");
        Speciality fin = new Speciality(IdGenerator.generateSpecialityId(), "Finance, Banking and Insurance");
        Speciality mng = new Speciality(IdGenerator.generateSpecialityId(), "Management");

        //Departments
        Department dep_et = new Department(IdGenerator.generateDepartmentId(), "Department of Economic Theory");
        Department dep_fin = new Department(IdGenerator.generateDepartmentId(), "Department of Finance");
        Department dep_mbm = new Department(IdGenerator.generateDepartmentId(), "Department of Marketing and Business Management");

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

        // ==========================================
        // 3. FACULTY OF HUMANITIES (FH)
        // ==========================================
        Faculty fh = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Humanities", "FH", "+38(044) 425-14-20", deanFH);

        //Specialities
        Speciality hist = new Speciality(IdGenerator.generateSpecialityId(), "History and Archaeology");
        Speciality phil = new Speciality(IdGenerator.generateSpecialityId(), "Philosophy");
        Speciality cult = new Speciality(IdGenerator.generateSpecialityId(), "Cultural Studies");
        Speciality ling = new Speciality(IdGenerator.generateSpecialityId(), "Philology");

        //Departments
        Department dep_hist = new Department(IdGenerator.generateDepartmentId(), "Department of History");
        Department dep_arch = new Department(IdGenerator.generateDepartmentId(), "Department of Archaeology");
        Department dep_phil = new Department(IdGenerator.generateDepartmentId(), "Department of Philosophy and Religious Studies");
        Department dep_cult = new Department(IdGenerator.generateDepartmentId(), "Department of Cultural Studies");
        Department dep_lit = new Department(IdGenerator.generateDepartmentId(), "Department of Literature");
        Department dep_engl = new Department(IdGenerator.generateDepartmentId(), "Department of English Language");

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

        // ==========================================
        // 4. FACULTY OF LAW (FL)
        // ==========================================
        Faculty fl = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Law", "FL", "+38(044) 425-60-73", deanFL);

        //Specialities
        Speciality law = new Speciality(IdGenerator.generateSpecialityId(), "Law");
        Speciality pma = new Speciality(IdGenerator.generateSpecialityId(), "Public Management and Administration");

        //Departments
        Department dep_gjd = new Department(IdGenerator.generateDepartmentId(), "Department of General Juridical Disciplines");
        Department dep_iel = new Department(IdGenerator.generateDepartmentId(), "Department of International and European Law");
        Department dep_pl = new Department(IdGenerator.generateDepartmentId(), "Department of Public Law");
        Department dep_prl = new Department(IdGenerator.generateDepartmentId(), "Department of Private Law");

        //Specialities of Faculty FL
        fl.getSpeciality().add(law);
        fl.getSpeciality().add(pma);

        //Departments of Faculty FL
        fl.getDepartments().add(dep_gjd);
        fl.getDepartments().add(dep_iel);
        fl.getDepartments().add(dep_pl);
        fl.getDepartments().add(dep_prl);

        university.getFaculties().add(fl);

        // ==========================================
        // 5. FACULTY OF NATURAL SCIENCES (FNS)
        // ==========================================
        Faculty fns = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Natural Sciences", "FNS", "+38(044) 425-60-57", deanFNS);

        //Specialities
        Speciality bio = new Speciality(IdGenerator.generateSpecialityId(), "Biology and Biotechnology");
        Speciality eco = new Speciality(IdGenerator.generateSpecialityId(), "Ecology");
        Speciality chem = new Speciality(IdGenerator.generateSpecialityId(), "Chemistry");
        Speciality phys = new Speciality(IdGenerator.generateSpecialityId(), "Physics and Astronomy");

        //Departments
        Department dep_bio = new Department(IdGenerator.generateDepartmentId(), "Department of Biology");
        Department dep_eco = new Department(IdGenerator.generateDepartmentId(), "Department of Environmental Studies");
        Department dep_chem = new Department(IdGenerator.generateDepartmentId(), "Department of Chemistry");
        Department dep_pms = new Department(IdGenerator.generateDepartmentId(), "Department of Physical and Mathematical Sciences");

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

        // ==========================================
        // 6. FACULTY OF SOCIAL SCIENCES AND SOCIAL TECHNOLOGIES (FSSST)
        // ==========================================
        Faculty fssst = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Social Sciences and Social Technologies", "FSSST", "+38(044) 425-60-47", deanFSSST);

        //Specialities
        Speciality soc = new Speciality(IdGenerator.generateSpecialityId(), "Sociology");
        Speciality pol = new Speciality(IdGenerator.generateSpecialityId(), "Political Science");
        Speciality psy = new Speciality(IdGenerator.generateSpecialityId(), "Psychology");
        Speciality sw = new Speciality(IdGenerator.generateSpecialityId(), "Social Work");
        Speciality jour = new Speciality(IdGenerator.generateSpecialityId(), "Journalism");
        Speciality ir = new Speciality(IdGenerator.generateSpecialityId(), "International Relations");

        //Departments
        Department dep_soc = new Department(IdGenerator.generateDepartmentId(), "Department of Sociology");
        Department dep_pol = new Department(IdGenerator.generateDepartmentId(), "Department of Political Science");
        Department dep_psy = new Department(IdGenerator.generateDepartmentId(), "Department of Psychology and Pedagogy");
        Department school_sw = new Department(IdGenerator.generateDepartmentId(), "School of Social Work");
        Department school_jour = new Department(IdGenerator.generateDepartmentId(), "Mohyla School of Journalism");
        Department school_ph = new Department(IdGenerator.generateDepartmentId(), "School of Public Health");

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

        // ==========================================
        // FI (faculty of informatics)
        // ==========================================
        Teacher headD001 = new Teacher(IdGenerator.generateTeacherId(), "Semen", "Horokhovskyi", "Samuilovych", "Head of Department", null); // Department of Computer Science
        Teacher headD002 = new Teacher(IdGenerator.generateTeacherId(), "Oleksandr", "Zhezherun", "Petrovych", "Head of Department", null); // Department of Multimedia Systems
        Teacher headD003 = new Teacher(IdGenerator.generateTeacherId(), "Ihor", "Derevianko", "Mykolaiovych", "Head of Department", null); // Department of Automation, Computer-Integrated Technologies, and Robotics
        Teacher headD004 = new Teacher(IdGenerator.generateTeacherId(), "Ruslan", "Chornei", "Kostiantynovych", "Head of Department", null); // Department of Mathematics


        // ==========================================
        // FE (faculty of economics)
        // ==========================================
        Teacher headD005 = new Teacher(IdGenerator.generateTeacherId(), "Yurii", "Bazhal", "Mykolaiovych", "Head of Department", null); // Department of Economic Theory
        Teacher headD006 = new Teacher(IdGenerator.generateTeacherId(), "Iryna", "Lukianenko", "Hryhorivna", "Head of Department", null); // Department of Finance
        Teacher headD007 = new Teacher(IdGenerator.generateTeacherId(), "Oleksandra", "Humenna", "Vitaliivna", "Head of Department", null); // Department of Marketing and Business Management

        // ==========================================
        // FH (faculty of humanities)
        // ==========================================
        Teacher headD008 = new Teacher(IdGenerator.generateTeacherId(), "Nataliya", "Shlikhta", "Vasylivna", "Head of Department", null); // Department of History
        Teacher headD009 = new Teacher(IdGenerator.generateTeacherId(), "Leonid", "Zalizniak", "Lvovych", "Head of Department", null); // Department of Archaeology
        Teacher headD010 = new Teacher(IdGenerator.generateTeacherId(), "Vadym", "Menzhulin", "Ihorovych", "Head of Department", null); // Department of Philosophy and Religious Studies
        Teacher headD011 = new Teacher(IdGenerator.generateTeacherId(), "Roman", "Veretelnyk", "Mykolaiovych", "Head of Department", null); // Department of Cultural Studies
        Teacher headD012 = new Teacher(IdGenerator.generateTeacherId(), "Vira", "Aheieva", "Pavlivna", "Head of Department", null); // Department of Literature
        Teacher headD013 = new Teacher(IdGenerator.generateTeacherId(), "Svitlana", "Ivanenko", "Ihorivna", "Head of Department", null); // Department of English Language

        // ==========================================
        // FL (faculty of law)
        // ==========================================
        Teacher headD014 = new Teacher(IdGenerator.generateTeacherId(), "Mykola", "Koziubra", "Ivanovych", "Head of Department", null); // Department of General Juridical Disciplines
        Teacher headD015 = new Teacher(IdGenerator.generateTeacherId(), "Myroslava", "Antonovych", "Orestivna", "Head of Department", null); // Department of International and European Law
        Teacher headD016 = new Teacher(IdGenerator.generateTeacherId(), "Volodymyr", "Venher", "Mykolaiovych", "Head of Department", null); // Department of Public Law
        Teacher headD017 = new Teacher(IdGenerator.generateTeacherId(), "Zoryana", "Borysenko", "Serhiivna", "Head of Department", null); // Department of Private Law

        // ==========================================
        // FNS (faculty of natural sciences)
        // ==========================================
        Teacher headD018 = new Teacher(IdGenerator.generateTeacherId(), "Taras", "Kazantsev", "Anatoliyovych", "Head of Department", null); // Department of Biology
        Teacher headD019 = new Teacher(IdGenerator.generateTeacherId(), "Viktor", "Karamushka", "Mykolaiovych", "Head of Department", null); // Department of Environmental Studies
        Teacher headD020 = new Teacher(IdGenerator.generateTeacherId(), "Anatolii", "Burban", "Fedorovych", "Head of Department", null); // Department of Chemistry
        Teacher headD021 = new Teacher(IdGenerator.generateTeacherId(), "Bohdan", "Kopyilets", "Ivanovych", "Head of Department", null); // Department of Physical and Mathematical Sciences

        // ==========================================
        // FSSST (faculty of social sciences and social technologies)
        // ==========================================
        Teacher headD022 = new Teacher(IdGenerator.generateTeacherId(), "Svitlana", "Oksamytna", "Mykolaivna", "Head of Department", null); // Department of Sociology
        Teacher headD023 = new Teacher(IdGenerator.generateTeacherId(), "Oleksandr", "Demianchuk", "Petrovych", "Head of Department", null); // Department of Political Science
        Teacher headD024 = new Teacher(IdGenerator.generateTeacherId(), "Serhii", "Bohdanov", "Bohdanovych", "Head of Department", null); // Department of Psychology and Pedagogy
        Teacher headD025 = new Teacher(IdGenerator.generateTeacherId(), "Oksana", "Boiko", "Mykolaivna", "Head of Department", null); // School of Social Work
        Teacher headD026 = new Teacher(IdGenerator.generateTeacherId(), "Yevhen", "Fedchenko", "Mykolaiovych", "Head of Department", null); // Mohyla School of Journalism
        Teacher headD027 = new Teacher(IdGenerator.generateTeacherId(), "Tetiana", "Yurochko", "Petrivna", "Head of Department", null); // School of Public Health

        Map<String, Teacher> departmentHeads = new HashMap<>();

        // FI(faculty of informatics)
        departmentHeads.put("d001", headD001);
        departmentHeads.put("d002", headD002);
        departmentHeads.put("d003", headD003);
        departmentHeads.put("d004", headD004);

        // FE (faculty of economics)
        departmentHeads.put("d005", headD005);
        departmentHeads.put("d006", headD006);
        departmentHeads.put("d007", headD007);

        // FH (faculty of humanities)
        departmentHeads.put("d008", headD008);
        departmentHeads.put("d009", headD009);
        departmentHeads.put("d010", headD010);
        departmentHeads.put("d011", headD011);
        departmentHeads.put("d012", headD012);
        departmentHeads.put("d013", headD013);

        // FL (faculty of law)
        departmentHeads.put("d014", headD014);
        departmentHeads.put("d015", headD015);
        departmentHeads.put("d016", headD016);
        departmentHeads.put("d017", headD017);

        // FNS (faculty of natural sciences)
        departmentHeads.put("d018", headD018);
        departmentHeads.put("d019", headD019);
        departmentHeads.put("d020", headD020);
        departmentHeads.put("d021", headD021);

        // FSSST (faculty of social sciences and social technologies)
        departmentHeads.put("d022", headD022);
        departmentHeads.put("d023", headD023);
        departmentHeads.put("d024", headD024);
        departmentHeads.put("d025", headD025);
        departmentHeads.put("d026", headD026);
        departmentHeads.put("d027", headD027);
        // Persisting should happen on explicit user actions (or logout), not on startup initialization.

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                String deptId = dept.getId();
                if (departmentHeads.containsKey(deptId)) {
                    dept.setHead(departmentHeads.get(deptId));
                }
            }
        }

    }
}