package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminRightsTest {
    //set up test environment
    @BeforeEach
    void setUp() {
        UserService.getAllUsers().clear();
        UserService.registerNewUser("testUser", "1234", Role.USER);
    }

    //test edit user role (success)
    @Test
    void testEditUser_RoleUpdatedSuccessfully(){
        UserService.editUser("testUser", Role.MANAGER);
        List<User> users = UserService.getAllUsers();
        assertEquals(Role.MANAGER, users.get(0).getRole(), "Role should be updated to MANAGER");
    }

    //test edit user role (failure, same role)
    @Test
    void testEditUser_RoleNotUpdated(){
        UserService.editUser("testUser", Role.USER);
        List<User> users = UserService.getAllUsers();
        assertEquals(Role.USER, users.get(0).getRole(), "Role should not be updated");
    }

    //test edit user role (failure, user not found)
    @Test
    void testEditUser_UserNotFound(){
        UserService.editUser("nonExistentUser", Role.MANAGER);
        List<User> users = UserService.getAllUsers();
        assertEquals(1, users.size(), "Only one user should be found");
        assertEquals(Role.USER, users.get(0).getRole(), "Role should not be updated");
    }
}
