package person;

import faculty.Faculty;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.NetworkClient;
import service.Request;
import service.Response;
import speciality.Speciality;
import ui.StudentCardWindow;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

class ModStudentUtilsSearchCardTest {

    @Test
    void searchStudentMenuShowsCardForAllSearchModesWhenSelectionChosen() {
        Student student = createStudent();
        Response ok = new Response(true, "OK", List.of(student));

        String[] inputs = {
                "1\nJohn\n\n1\n",   // by name
                "2\n1\n\n1\n",      // by group
                "3\n1\n\n1\n",      // by course
                "4\nsp001\n\n1\n",  // by speciality
                "5\n\n1\n"           // show all
        };

        for (String input : inputs) {
            try (MockedStatic<NetworkClient> netMock = Mockito.mockStatic(NetworkClient.class);
                 MockedStatic<StudentCardWindow> cardMock = Mockito.mockStatic(StudentCardWindow.class)) {
                netMock.when(() -> NetworkClient.sendRequest(Mockito.any(Request.class))).thenReturn(ok);

                Scanner scanner = new Scanner(input);
                assertDoesNotThrow(() -> ModStudentUtils.searchStudentMenu(scanner, true));

                cardMock.verify(() -> StudentCardWindow.open(student, true), times(1));
            }
        }
    }

    @Test
    void searchStudentMenuSkipsCardWhenUserChoosesZero() {
        Student student = createStudent();
        Response ok = new Response(true, "OK", List.of(student));

        try (MockedStatic<NetworkClient> netMock = Mockito.mockStatic(NetworkClient.class);
             MockedStatic<StudentCardWindow> cardMock = Mockito.mockStatic(StudentCardWindow.class)) {
            netMock.when(() -> NetworkClient.sendRequest(Mockito.any(Request.class))).thenReturn(ok);

            Scanner scanner = new Scanner("1\nJohn\n\n0\n");
            assertDoesNotThrow(() -> ModStudentUtils.searchStudentMenu(scanner, false));

            cardMock.verifyNoInteractions();
        }
    }

    private Student createStudent() {
        Faculty faculty = new Faculty("f001", "Faculty", "F", "contacts", null);
        Speciality speciality = new Speciality("sp001", "Speciality");
        return new Student("st20260001", "John", "Doe", "Smith", LocalDate.of(2025, 9, 1), 1, faculty, speciality, StudyForm.BUDGET);
    }
}

