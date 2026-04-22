package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import user.UserService;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchUtilsRoleGateTest {

    private final UserService userService = UserService.getInstance();

    @AfterEach
    void tearDown() {
        userService.logout();
    }

    @Test
    void canCurrentUserWriteIsTrueOnlyForManagerOrAdmin() throws Exception {
        userService.logout();
        assertFalse(invokeCanCurrentUserWrite());

        userService.loginSuccess("user", "user");
        assertFalse(invokeCanCurrentUserWrite());

        userService.loginSuccess("manager", "manager");
        assertTrue(invokeCanCurrentUserWrite());

        userService.loginSuccess("admin", "admin");
        assertTrue(invokeCanCurrentUserWrite());
    }

    private boolean invokeCanCurrentUserWrite() throws Exception {
        Method method = SearchUtils.class.getDeclaredMethod("canCurrentUserWrite");
        method.setAccessible(true);
        return (boolean) method.invoke(null);
    }
}

