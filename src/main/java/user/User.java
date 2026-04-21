package user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

// User entity
@Data
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Role role;
    private int permissionMask;

    //methods for working with mask
    //add
    public void grantPermission(int perm){
        this.permissionMask|= perm;
    }
    //remove
    public void revokePermission(int perm){
        this.permissionMask &= ~ perm;
    }
    //check
    public boolean hasPermission(int perm){
        return (this.permissionMask & perm) != 0;
    }
}