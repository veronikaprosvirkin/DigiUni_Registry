import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private UserService userService;

    // Reset static fields before each test to prevent cross-test contamination
    @BeforeEach
    void setUp() throws Exception {
        // Clear static users list
        Field usersField = UserService.class.getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(null, new ArrayList<>());

        // Clear static currentUser
        Field currentUserField = UserService.class.getDeclaredField("currentUser");
        currentUserField.setAccessible(true);
        currentUserField.set(null, null);

        // Init UserService (adds admin, user, manager)
        userService = new UserService();
    }

    // Test successful login
    @Test
    void testLoginSuccess() {
        boolean result = UserService.loginSuccess("admin", "admin");
        assertTrue(result);
        assertNotNull(userService.getCurrentUser());
        assertEquals("admin", userService.getCurrentUser().getUsername());
    }

    // Test failed login
    @Test
    void testLoginFail() {
        boolean result = UserService.loginSuccess("admin", "wrongpass");
        assertFalse(result);
        assertNull(userService.getCurrentUser());
    }

    // Test logout
    @Test
    void testLogout() {
        UserService.loginSuccess("user", "user");
        UserService.logout();
        assertNull(userService.getCurrentUser());
    }

    // Test register new user
    @Test
    void testRegisterNewUserSuccess() {
        UserService.registerNewUser("Kowalski", "haslo123", Role.USER);
        List<User> users = UserService.getAllUsers();

        assertEquals(4, users.size());
        assertEquals("Kowalski", users.get(3).getUsername());
    }

    // Test register duplicate user
    @Test
    void testRegisterNewUserDuplicate() {
        UserService.registerNewUser("user", "newpass", Role.MANAGER);
        List<User> users = UserService.getAllUsers();

        assertEquals(3, users.size()); // Size remains the same
    }

    // Test delete regular user
    @Test
    void testDeleteUserSuccess() {
        UserService.deleteUser("user");
        List<User> users = UserService.getAllUsers();

        assertEquals(2, users.size());
        assertFalse(users.stream().anyMatch(u -> u.getUsername().equals("user")));
    }

    // Test delete admin restriction
    @Test
    void testDeleteAdmin() {
        UserService.deleteUser("admin");
        List<User> users = UserService.getAllUsers();

        assertEquals(3, users.size()); // Admin cannot be deleted
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("admin")));
    }

    // Test edit user role
    @Test
    void testEditUserRoleSuccess() {
        UserService.editUser("user", Role.MANAGER);
        User editedUser = UserService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("user"))
                .findFirst()
                .orElse(null);

        assertNotNull(editedUser);
        assertEquals(Role.MANAGER, editedUser.getRole());
    }

    // Test edit non-existent user
    @Test
    void testEditUserNotFound() {
        UserService.editUser("Nowak", Role.ADMIN);
        List<User> users = UserService.getAllUsers();

        assertEquals(3, users.size()); // Nothing changes
    }
}