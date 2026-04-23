package person;

import department.Department;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.NetworkClient;
import service.Request;
import service.Response;
import ui.TeacherCardWindow;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

class ModTeacherUtilsSearchCardTest {

    @Test
    void searchTeacherMenuShowsCardForAllSearchModesWhenSelectionChosen() {
        Teacher teacher = createTeacher();
        Response ok = new Response(true, "OK", List.of(teacher));

        String[] inputs = {
                "1\nJohn\n\n1\n",  // by name
                "2\nt0001\n\n1\n", // by id
                "3\nDepartment\n\n1\n",  // by department
                "4\n\n1\n"          // show all
        };

        for (String input : inputs) {
            try (MockedStatic<NetworkClient> netMock = Mockito.mockStatic(NetworkClient.class);
                 MockedStatic<TeacherCardWindow> cardMock = Mockito.mockStatic(TeacherCardWindow.class)) {
                netMock.when(() -> NetworkClient.sendRequest(Mockito.any(Request.class))).thenReturn(ok);

                Scanner scanner = new Scanner(input);
                assertDoesNotThrow(() -> ModTeacherUtils.searchTeacherMenu(scanner, true));

                cardMock.verify(() -> TeacherCardWindow.open(teacher, true), times(1));
            }
        }
    }

    @Test
    void searchTeacherMenuSkipsCardWhenUserChoosesZero() {
        Teacher teacher = createTeacher();
        Response ok = new Response(true, "OK", List.of(teacher));

        try (MockedStatic<NetworkClient> netMock = Mockito.mockStatic(NetworkClient.class);
             MockedStatic<TeacherCardWindow> cardMock = Mockito.mockStatic(TeacherCardWindow.class)) {
            netMock.when(() -> NetworkClient.sendRequest(Mockito.any(Request.class))).thenReturn(ok);

            Scanner scanner = new Scanner("1\nJohn\n\n0\n");
            assertDoesNotThrow(() -> ModTeacherUtils.searchTeacherMenu(scanner, false));

            cardMock.verifyNoInteractions();
        }
    }

    private Teacher createTeacher() {
        Department department = new Department("d001", "Department");
        return new Teacher("t0001", "John", "Doe", "Smith", Position.PROFESSOR, department);
    }
}



