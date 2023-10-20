package Requests;

/**
 * LoginRequest object that has username and password stored in it
 */
public class LoginRequest {
    private String username;
    private String password;
    public LoginRequest() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password= password;
    }
}