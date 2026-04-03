package user;

import lombok.AllArgsConstructor;
import lombok.Data;

// User entity
@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private Role role;
}