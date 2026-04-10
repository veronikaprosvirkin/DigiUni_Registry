package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdminRightsTest {
    private UserService userService;

    // set up test environment
    @BeforeEach
    void setUp() {
        userService = UserService.createTestInstance();
        userService.registerNewUser("testUser", "1234", Role.USER);
    }

    // test edit user role (success)
    @Test
    void testEditUser_RoleUpdatedSuccessfully() {
        userService.editUser("testUser", Role.MANAGER);
        User testUser = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("testUser"))
                .findFirst()
                .orElse(null);

        assertNotNull(testUser);
        assertEquals(Role.MANAGER, testUser.getRole(), "Role should be updated to MANAGER");
    }

    // test edit user role (failure, same role)
    @Test
    void testEditUser_RoleNotUpdated() {
        userService.editUser("testUser", Role.USER);
        User testUser = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("testUser"))
                .findFirst()
                .orElse(null);

        assertNotNull(testUser);
        assertEquals(Role.USER, testUser.getRole(), "Role should not be updated");
    }

    // test edit user role (failure, user not found)
    @Test
    void testEditUser_UserNotFound() {
        userService.editUser("nonExistentUser", Role.MANAGER);
        List<User> users = userService.getAllUsers();
        User testUser = users.stream()
                .filter(u -> u.getUsername().equals("testUser"))
                .findFirst()
                .orElse(null);

        assertNotNull(testUser);
        assertEquals(Role.USER, testUser.getRole(), "Role should not be updated");
    }
}
