package university;

import person.Teacher;
import person.Position;
import repository.TeacherRepository;
import utils.IdGenerator;
import faculty.Faculty;
import speciality.Speciality;
import department.Department;

import java.util.Objects;

public class UniversityService {
    private final University university;
    private final TeacherRepository teacherRepository;

    public UniversityService(University university, TeacherRepository teacherRepository) {
        this.university = Objects.requireNonNull(university, "university must not be null");
        this.teacherRepository = teacherRepository;
        initializeStructure();
    }

    private void initializeStructure() {
        if (!university.getFaculties().isEmpty()) {
            return; // Data already loaded
        }

        // ==========================================
        // 1. FACULTY OF INFORMATICS (FI)
        // ==========================================
        Teacher deanFI = new Teacher(IdGenerator.generateTeacherId(), "Andrii", "Hlybovets", "Mykolaiovych", Position.DEAN, null);
        teacherRepository.save(deanFI);
        Faculty fi = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Informatics", "FI", "+38(044) 425-60-64", deanFI);

        // Specialities
        Speciality se = new Speciality(IdGenerator.generateSpecialityId(), "Software Engineering");
        Speciality cs = new Speciality(IdGenerator.generateSpecialityId(), "Computer Science");
        Speciality acitr = new Speciality(IdGenerator.generateSpecialityId(), "Automation, Computer-Integrated Technologies, and Robotics");
        Speciality ap = new Speciality(IdGenerator.generateSpecialityId(), "Applied Mathematics");
        Speciality sa = new Speciality(IdGenerator.generateSpecialityId(), "Systems Analysis");

        fi.getSpeciality().add(se);
        fi.getSpeciality().add(cs);
        fi.getSpeciality().add(acitr);
        fi.getSpeciality().add(ap);
        fi.getSpeciality().add(sa);

        Department dep_cs = new Department(IdGenerator.generateDepartmentId(), "Department of Computer Science");
        Teacher headD001 = new Teacher(IdGenerator.generateTeacherId(), "Semen", "Horokhovskyi", "Samuilovych", Position.HEAD_OF_DEPARTMENT, dep_cs);
        teacherRepository.save(headD001);
        dep_cs.setHead(headD001);

        Department dep_ms = new Department(IdGenerator.generateDepartmentId(), "Department of Multimedia Systems");
        Teacher headD002 = new Teacher(IdGenerator.generateTeacherId(), "Oleksandr", "Zhezherun", "Petrovych", Position.HEAD_OF_DEPARTMENT, dep_ms);
        teacherRepository.save(headD002);
        dep_ms.setHead(headD002);

        Department dep_acitr = new Department(IdGenerator.generateDepartmentId(), "Department of Automation, Computer-Integrated Technologies, and Robotics");
        Teacher headD003 = new Teacher(IdGenerator.generateTeacherId(), "Ihor", "Derevianko", "Mykolaiovych", Position.HEAD_OF_DEPARTMENT, dep_acitr);
        teacherRepository.save(headD003);
        dep_acitr.setHead(headD003);

        Department dep_math = new Department(IdGenerator.generateDepartmentId(), "Department of Mathematics");
        Teacher headD004 = new Teacher(IdGenerator.generateTeacherId(), "Ruslan", "Chornei", "Kostiantynovych", Position.HEAD_OF_DEPARTMENT, dep_math);
        teacherRepository.save(headD004);
        dep_math.setHead(headD004);

        fi.getDepartments().add(dep_cs);
        fi.getDepartments().add(dep_ms);
        fi.getDepartments().add(dep_acitr);
        fi.getDepartments().add(dep_math);

        university.getFaculties().add(fi);

        // ==========================================
        // 2. FACULTY OF ECONOMICS (FE)
        // ==========================================
        Teacher deanFE = new Teacher(IdGenerator.generateTeacherId(), "Oleksandra", "Humenna", "Vitaliivna", Position.DEAN, null);
        teacherRepository.save(deanFE);
        Faculty fen = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Economics", "FE", "+38(044) 425-60-59", deanFE);

        // Specialities
        Speciality ma = new Speciality(IdGenerator.generateSpecialityId(), "Marketing");
        Speciality econ = new Speciality(IdGenerator.generateSpecialityId(), "Economics");
        Speciality fin = new Speciality(IdGenerator.generateSpecialityId(), "Finance, Banking and Insurance");
        Speciality mng = new Speciality(IdGenerator.generateSpecialityId(), "Management");

        fen.getSpeciality().add(ma);
        fen.getSpeciality().add(econ);
        fen.getSpeciality().add(fin);
        fen.getSpeciality().add(mng);

        // Departments & Heads
        Department dep_et = new Department(IdGenerator.generateDepartmentId(), "Department of Economic Theory");
        Teacher headD005 = new Teacher(IdGenerator.generateTeacherId(), "Yurii", "Bazhal", "Mykolaiovych", Position.HEAD_OF_DEPARTMENT, dep_et);
        teacherRepository.save(headD005);
        dep_et.setHead(headD005);

        Department dep_fin = new Department(IdGenerator.generateDepartmentId(), "Department of Finance");
        Teacher headD006 = new Teacher(IdGenerator.generateTeacherId(), "Iryna", "Lukianenko", "Hryhorivna", Position.HEAD_OF_DEPARTMENT, dep_fin);
        teacherRepository.save(headD006);
        dep_fin.setHead(headD006);

        Department dep_mbm = new Department(IdGenerator.generateDepartmentId(), "Department of Marketing and Business Management");
        deanFE.setDepartment(dep_mbm);
        dep_mbm.setHead(deanFE);

        fen.getDepartments().add(dep_et);
        fen.getDepartments().add(dep_fin);
        fen.getDepartments().add(dep_mbm);

        university.getFaculties().add(fen);

        // ==========================================
        // 3. FACULTY OF HUMANITIES (FH)
        // ==========================================
        Teacher deanFH = new Teacher(IdGenerator.generateTeacherId(), "Dmytro", "Mazin", "Mykhailovych", Position.DEAN, null);
        teacherRepository.save(deanFH);
        Faculty fh = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Humanities", "FH", "+38(044) 425-14-20", deanFH);

        Speciality hist = new Speciality(IdGenerator.generateSpecialityId(), "History and Archaeology");
        Speciality phil = new Speciality(IdGenerator.generateSpecialityId(), "Philosophy");
        Speciality cult = new Speciality(IdGenerator.generateSpecialityId(), "Cultural Studies");
        Speciality ling = new Speciality(IdGenerator.generateSpecialityId(), "Philology");

        fh.getSpeciality().add(hist);
        fh.getSpeciality().add(phil);
        fh.getSpeciality().add(cult);
        fh.getSpeciality().add(ling);

        Department dep_hist = new Department(IdGenerator.generateDepartmentId(), "Department of History");
        Teacher headD008 = new Teacher(IdGenerator.generateTeacherId(), "Nataliya", "Shlikhta", "Vasylivna", Position.HEAD_OF_DEPARTMENT, dep_hist);
        teacherRepository.save(headD008);
        dep_hist.setHead(headD008);

        Department dep_arch = new Department(IdGenerator.generateDepartmentId(), "Department of Archaeology");
        Teacher headD009 = new Teacher(IdGenerator.generateTeacherId(), "Leonid", "Zalizniak", "Lvovych", Position.HEAD_OF_DEPARTMENT, dep_arch);
        teacherRepository.save(headD009);
        dep_arch.setHead(headD009);

        Department dep_phil = new Department(IdGenerator.generateDepartmentId(), "Department of Philosophy and Religious Studies");
        Teacher headD010 = new Teacher(IdGenerator.generateTeacherId(), "Vadym", "Menzhulin", "Ihorovych", Position.HEAD_OF_DEPARTMENT, dep_phil);
        teacherRepository.save(headD010);
        dep_phil.setHead(headD010);

        Department dep_cult = new Department(IdGenerator.generateDepartmentId(), "Department of Cultural Studies");
        Teacher headD011 = new Teacher(IdGenerator.generateTeacherId(), "Roman", "Veretelnyk", "Mykolaiovych", Position.HEAD_OF_DEPARTMENT, dep_cult);
        teacherRepository.save(headD011);
        dep_cult.setHead(headD011);

        Department dep_lit = new Department(IdGenerator.generateDepartmentId(), "Department of Literature");
        Teacher headD012 = new Teacher(IdGenerator.generateTeacherId(), "Vira", "Aheieva", "Pavlivna", Position.HEAD_OF_DEPARTMENT, dep_lit);
        teacherRepository.save(headD012);
        dep_lit.setHead(headD012);

        Department dep_engl = new Department(IdGenerator.generateDepartmentId(), "Department of English Language");
        Teacher headD013 = new Teacher(IdGenerator.generateTeacherId(), "Svitlana", "Ivanenko", "Ihorivna", Position.HEAD_OF_DEPARTMENT, dep_engl);
        teacherRepository.save(headD013);
        dep_engl.setHead(headD013);

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
        Teacher deanFL = new Teacher(IdGenerator.generateTeacherId(), "Volodymyr", "Venher", "Mykolaiovych", Position.DEAN, null);
        teacherRepository.save(deanFL);
        Faculty fl = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Law", "FL", "+38(044) 425-60-73", deanFL);

        Speciality law = new Speciality(IdGenerator.generateSpecialityId(), "Law");
        Speciality pma = new Speciality(IdGenerator.generateSpecialityId(), "Public Management and Administration");

        fl.getSpeciality().add(law);
        fl.getSpeciality().add(pma);

        Department dep_gjd = new Department(IdGenerator.generateDepartmentId(), "Department of General Juridical Disciplines");
        Teacher headD014 = new Teacher(IdGenerator.generateTeacherId(), "Mykola", "Koziubra", "Ivanovych", Position.HEAD_OF_DEPARTMENT, dep_gjd);
        teacherRepository.save(headD014);
        dep_gjd.setHead(headD014);

        Department dep_iel = new Department(IdGenerator.generateDepartmentId(), "Department of International and European Law");
        Teacher headD015 = new Teacher(IdGenerator.generateTeacherId(), "Myroslava", "Antonovych", "Orestivna", Position.HEAD_OF_DEPARTMENT, dep_iel);
        teacherRepository.save(headD015);
        dep_iel.setHead(headD015);

        Department dep_pl = new Department(IdGenerator.generateDepartmentId(), "Department of Public Law");
        deanFL.setDepartment(dep_pl);
        dep_pl.setHead(deanFL);

        Department dep_prl = new Department(IdGenerator.generateDepartmentId(), "Department of Private Law");
        Teacher headD017 = new Teacher(IdGenerator.generateTeacherId(), "Zoryana", "Borysenko", "Serhiivna", Position.HEAD_OF_DEPARTMENT, dep_prl);
        teacherRepository.save(headD017);
        dep_prl.setHead(headD017);

        fl.getDepartments().add(dep_gjd);
        fl.getDepartments().add(dep_iel);
        fl.getDepartments().add(dep_pl);
        fl.getDepartments().add(dep_prl);

        university.getFaculties().add(fl);

        // ==========================================
        // 5. FACULTY OF NATURAL SCIENCES (FNS)
        // ==========================================
        Teacher deanFNS = new Teacher(IdGenerator.generateTeacherId(), "Anatolii", "Bilous", "Markovych", Position.DEAN, null);
        teacherRepository.save(deanFNS);
        Faculty fns = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Natural Sciences", "FNS", "+38(044) 425-60-57", deanFNS);

        Speciality bio = new Speciality(IdGenerator.generateSpecialityId(), "Biology and Biotechnology");
        Speciality eco = new Speciality(IdGenerator.generateSpecialityId(), "Ecology");
        Speciality chem = new Speciality(IdGenerator.generateSpecialityId(), "Chemistry");
        Speciality phys = new Speciality(IdGenerator.generateSpecialityId(), "Physics and Astronomy");

        fns.getSpeciality().add(bio);
        fns.getSpeciality().add(eco);
        fns.getSpeciality().add(chem);
        fns.getSpeciality().add(phys);

        Department dep_bio = new Department(IdGenerator.generateDepartmentId(), "Department of Biology");
        Teacher headD018 = new Teacher(IdGenerator.generateTeacherId(), "Taras", "Kazantsev", "Anatoliyovych", Position.HEAD_OF_DEPARTMENT, dep_bio);
        teacherRepository.save(headD018);
        dep_bio.setHead(headD018);

        Department dep_eco = new Department(IdGenerator.generateDepartmentId(), "Department of Environmental Studies");
        Teacher headD019 = new Teacher(IdGenerator.generateTeacherId(), "Viktor", "Karamushka", "Mykolaiovych", Position.HEAD_OF_DEPARTMENT, dep_eco);
        teacherRepository.save(headD019);
        dep_eco.setHead(headD019);

        Department dep_chem = new Department(IdGenerator.generateDepartmentId(), "Department of Chemistry");
        Teacher headD020 = new Teacher(IdGenerator.generateTeacherId(), "Anatolii", "Burban", "Fedorovych", Position.HEAD_OF_DEPARTMENT, dep_chem);
        teacherRepository.save(headD020);
        dep_chem.setHead(headD020);

        Department dep_pms = new Department(IdGenerator.generateDepartmentId(), "Department of Physical and Mathematical Sciences");
        Teacher headD021 = new Teacher(IdGenerator.generateTeacherId(), "Bohdan", "Kopyilets", "Ivanovych", Position.HEAD_OF_DEPARTMENT, dep_pms);
        teacherRepository.save(headD021);
        dep_pms.setHead(headD021);

        fns.getDepartments().add(dep_bio);
        fns.getDepartments().add(dep_eco);
        fns.getDepartments().add(dep_chem);
        fns.getDepartments().add(dep_pms);

        university.getFaculties().add(fns);

        // ==========================================
        // 6. FACULTY OF SOCIAL SCIENCES AND SOCIAL TECHNOLOGIES (FSSST)
        // ==========================================
        Teacher deanFSSST = new Teacher(IdGenerator.generateTeacherId(), "Svitlana", "Oksamytna", "Mykolaivna", Position.DEAN, null);
        teacherRepository.save(deanFSSST);
        Faculty fssst = new Faculty(IdGenerator.generateFacultyId(), "Faculty of Social Sciences and Social Technologies", "FSSST", "+38(044) 425-60-47", deanFSSST);

        Speciality soc = new Speciality(IdGenerator.generateSpecialityId(), "Sociology");
        Speciality pol = new Speciality(IdGenerator.generateSpecialityId(), "Political Science");
        Speciality psy = new Speciality(IdGenerator.generateSpecialityId(), "Psychology");
        Speciality sw = new Speciality(IdGenerator.generateSpecialityId(), "Social Work");
        Speciality jour = new Speciality(IdGenerator.generateSpecialityId(), "Journalism");
        Speciality ir = new Speciality(IdGenerator.generateSpecialityId(), "International Relations");

        fssst.getSpeciality().add(soc);
        fssst.getSpeciality().add(pol);
        fssst.getSpeciality().add(psy);
        fssst.getSpeciality().add(sw);
        fssst.getSpeciality().add(jour);
        fssst.getSpeciality().add(ir);

        Department dep_soc = new Department(IdGenerator.generateDepartmentId(), "Department of Sociology");
        deanFSSST.setDepartment(dep_soc);
        dep_soc.setHead(deanFSSST);

        Department dep_pol = new Department(IdGenerator.generateDepartmentId(), "Department of Political Science");
        Teacher headD023 = new Teacher(IdGenerator.generateTeacherId(), "Oleksandr", "Demianchuk", "Petrovych", Position.HEAD_OF_DEPARTMENT, dep_pol);
        teacherRepository.save(headD023);
        dep_pol.setHead(headD023);

        Department dep_psy = new Department(IdGenerator.generateDepartmentId(), "Department of Psychology and Pedagogy");
        Teacher headD024 = new Teacher(IdGenerator.generateTeacherId(), "Serhii", "Bohdanov", "Bohdanovych", Position.HEAD_OF_DEPARTMENT, dep_psy);
        teacherRepository.save(headD024);
        dep_psy.setHead(headD024);

        Department school_sw = new Department(IdGenerator.generateDepartmentId(), "School of Social Work");
        Teacher headD025 = new Teacher(IdGenerator.generateTeacherId(), "Oksana", "Boiko", "Mykolaivna", Position.HEAD_OF_DEPARTMENT, school_sw);
        teacherRepository.save(headD025);
        school_sw.setHead(headD025);

        Department school_jour = new Department(IdGenerator.generateDepartmentId(), "Mohyla School of Journalism");
        Teacher headD026 = new Teacher(IdGenerator.generateTeacherId(), "Yevhen", "Fedchenko", "Mykolaiovych", Position.HEAD_OF_DEPARTMENT, school_jour);
        teacherRepository.save(headD026);
        school_jour.setHead(headD026);

        Department school_ph = new Department(IdGenerator.generateDepartmentId(), "School of Public Health");
        Teacher headD027 = new Teacher(IdGenerator.generateTeacherId(), "Tetiana", "Yurochko", "Petrivna", Position.HEAD_OF_DEPARTMENT, school_ph);
        teacherRepository.save(headD027);
        school_ph.setHead(headD027);

        fssst.getDepartments().add(dep_soc);
        fssst.getDepartments().add(dep_pol);
        fssst.getDepartments().add(dep_psy);
        fssst.getDepartments().add(school_sw);
        fssst.getDepartments().add(school_jour);
        fssst.getDepartments().add(school_ph);

        university.getFaculties().add(fssst);
    }
}