import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();
    private User currentUser = null;

    public UserService() { //test users
        users.add(new User("admin", "admin", Role.ADMIN));
        users.add(new User("user", "user", Role.USER));
        users.add(new User("manager", "manager", Role.MANAGER));
    }
    // method for login
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }
    //return current user to main
    public User getCurrentUser(){
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
