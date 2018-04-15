package net.bucssa.buassist.Bean.Request;

/**
 * Created by tjin3 on 2017/12/21.
 */

public class RegisterReq {

    /**
     * username :
     * password :
     * email :
     */

    private String username;
    private String password;
    private String email;

    public RegisterReq(String username, String password, String email) {
        setEmail(email);
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
