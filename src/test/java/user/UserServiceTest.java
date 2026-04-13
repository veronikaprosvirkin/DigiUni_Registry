package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Fresh service instance per test keeps state isolated.
        userService = UserService.createTestInstance();
    }

    // Test successful login
    @Test
    void testLoginSuccess() {
        boolean result = userService.loginSuccess(new LoginCredentials("admin", "admin"));
        assertTrue(result);
        assertNotNull(userService.getCurrentUser());
        assertEquals("admin", userService.getCurrentUser().getUsername());
    }

    // Test failed login
    @Test
    void testLoginFail() {
        boolean result = userService.loginSuccess(new LoginCredentials("admin", "wrongpass"));
        assertFalse(result);
        assertNull(userService.getCurrentUser());
    }

    // Test logout
    @Test
    void testLogout() {
        userService.loginSuccess(new LoginCredentials("user", "user"));
        userService.logout();
        assertNull(userService.getCurrentUser());
    }

    // Test register new user
    @Test
    void testRegisterNewUserSuccess() {
        userService.registerNewUser("Kowalski", "haslo123", Role.USER);
        List<User> users = userService.getAllUsers();

        assertEquals(4, users.size());
        assertEquals("Kowalski", users.get(3).getUsername());
    }

    // Test register duplicate user
    @Test
    void testRegisterNewUserDuplicate() {
        userService.registerNewUser("user", "newpass", Role.MANAGER);
        List<User> users = userService.getAllUsers();

        assertEquals(3, users.size()); // Size remains the same
    }

    // Test delete regular user
    @Test
    void testDeleteUserSuccess() {
        userService.deleteUser("user");
        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertFalse(users.stream().anyMatch(u -> u.getUsername().equals("user")));
    }

    // Test delete admin restriction
    @Test
    void testDeleteAdmin() {
        userService.deleteUser("admin");
        List<User> users = userService.getAllUsers();

        assertEquals(3, users.size()); // Admin cannot be deleted
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("admin")));
    }

    // Test edit user role
    @Test
    void testEditUserRoleSuccess() {
        userService.editUser("user", Role.MANAGER);
        User editedUser = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("user"))
                .findFirst()
                .orElse(null);

        assertNotNull(editedUser);
        assertEquals(Role.MANAGER, editedUser.getRole());
    }

    // Test edit non-existent user
    @Test
    void testEditUserNotFound() {
        userService.editUser("Nowak", Role.ADMIN);
        List<User> users = userService.getAllUsers();

        assertEquals(3, users.size()); // Nothing changes
    }
}