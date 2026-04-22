package user;

import java.io.Serializable;

public record LoginCredentials(String username, String password) implements Serializable {
        private static final long serialVersionUID = 1L;
}

