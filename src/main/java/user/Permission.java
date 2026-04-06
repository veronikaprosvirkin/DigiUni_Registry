package user;

public class Permission {
    public static final int READ = 1;
    public static final int WRITE = 1 <<1;
    public static final int DELETE =1 <<2;
    public static final int ADMIN = 1 << 3;

    public static int getDefaultMaskForRole(Role role){
        if (role == Role.ADMIN){
            return READ|WRITE|DELETE|ADMIN;
        } else if (role == Role.MANAGER) {
            return READ|WRITE;
        } else {
            return READ;
        }
    }
}
