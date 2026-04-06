package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    @BeforeEach
    void resetCounters() throws Exception {
        setCounter("studentCounter", 1);
        setCounter("teacherCounter", 1);
        setCounter("facultyCounter", 1);
        setCounter("departmentCounter", 1);
        setCounter("specialityCounter", 1);
    }

    @Test
    void generateMethodsUseExpectedFormatAndIncrement() {
        assertEquals("st20260001", IdGenerator.generateStudentId(2026));
        assertEquals("st20260002", IdGenerator.generateStudentId(2026));

        assertEquals("t0001", IdGenerator.generateTeacherId());
        assertEquals("t0002", IdGenerator.generateTeacherId());

        assertEquals("f001", IdGenerator.generateFacultyId());
        assertEquals("f002", IdGenerator.generateFacultyId());

        assertEquals("d001", IdGenerator.generateDepartmentId());
        assertEquals("d002", IdGenerator.generateDepartmentId());

        assertEquals("sp001", IdGenerator.generateSpecialityId());
        assertEquals("sp002", IdGenerator.generateSpecialityId());
    }

    @Test
    void studentCounterIsGlobalAcrossDifferentYears() {
        assertEquals("st20240001", IdGenerator.generateStudentId(2024));
        assertEquals("st20250002", IdGenerator.generateStudentId(2025));
        assertEquals("st20260003", IdGenerator.generateStudentId(2026));
    }

    @Test
    void updateFacultyCounterSkipsToNextAvailableValue() {
        IdGenerator.updateFacultyCounter("f010");
        assertEquals("f011", IdGenerator.generateFacultyId());

        IdGenerator.updateFacultyCounter("f005");
        assertEquals("f012", IdGenerator.generateFacultyId());

        IdGenerator.updateFacultyCounter("x999");
        assertEquals("f013", IdGenerator.generateFacultyId());
    }

    @Test
    void updateDepartmentCounterSkipsToNextAvailableValue() {
        IdGenerator.updateDepartmentCounter("d099");
        assertEquals("d100", IdGenerator.generateDepartmentId());

        IdGenerator.updateDepartmentCounter("dabc");
        assertEquals("d101", IdGenerator.generateDepartmentId());

        IdGenerator.updateDepartmentCounter(null);
        assertEquals("d102", IdGenerator.generateDepartmentId());
    }

    @Test
    void updateSpecialityCounterSkipsToNextAvailableValue() {
        IdGenerator.updateSpecialityCounter("sp120");
        assertEquals("sp121", IdGenerator.generateSpecialityId());

        IdGenerator.updateSpecialityCounter("sp002");
        assertEquals("sp122", IdGenerator.generateSpecialityId());

        IdGenerator.updateSpecialityCounter("spbad");
        assertEquals("sp123", IdGenerator.generateSpecialityId());
    }

    @Test
    void updateMethodsIgnoreInvalidIdsAndKeepMonotonicSequence() {
        IdGenerator.updateFacultyCounter(null);
        IdGenerator.updateFacultyCounter("");
        IdGenerator.updateFacultyCounter("faculty10");
        IdGenerator.updateFacultyCounter("f");
        IdGenerator.updateFacultyCounter("f-1");
        IdGenerator.updateFacultyCounter("fNaN");
        assertEquals("f001", IdGenerator.generateFacultyId());

        IdGenerator.updateDepartmentCounter("dept001");
        IdGenerator.updateDepartmentCounter("d");
        IdGenerator.updateDepartmentCounter("d+");
        IdGenerator.updateDepartmentCounter("dabc");
        assertEquals("d001", IdGenerator.generateDepartmentId());

        IdGenerator.updateSpecialityCounter("spec001");
        IdGenerator.updateSpecialityCounter("sp");
        IdGenerator.updateSpecialityCounter("sp+");
        IdGenerator.updateSpecialityCounter("spxyz");
        assertEquals("sp001", IdGenerator.generateSpecialityId());
    }

    @Test
    void updateMethodsAdvanceWhenEqualToCurrentCounter() {
        assertEquals("f001", IdGenerator.generateFacultyId());
        IdGenerator.updateFacultyCounter("f002");
        assertEquals("f003", IdGenerator.generateFacultyId());

        assertEquals("d001", IdGenerator.generateDepartmentId());
        IdGenerator.updateDepartmentCounter("d002");
        assertEquals("d003", IdGenerator.generateDepartmentId());

        assertEquals("sp001", IdGenerator.generateSpecialityId());
        IdGenerator.updateSpecialityCounter("sp002");
        assertEquals("sp003", IdGenerator.generateSpecialityId());
    }

    @Test
    void updateMethodsSupportLargeAndZeroPaddedValues() {
        IdGenerator.updateFacultyCounter("f0009");
        assertEquals("f010", IdGenerator.generateFacultyId());

        IdGenerator.updateDepartmentCounter("d1000");
        assertEquals("d1001", IdGenerator.generateDepartmentId());

        IdGenerator.updateSpecialityCounter("sp0123");
        assertEquals("sp124", IdGenerator.generateSpecialityId());
    }

    private void setCounter(String fieldName, int value) throws Exception {
        Field field = IdGenerator.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(null, value);
    }
}

