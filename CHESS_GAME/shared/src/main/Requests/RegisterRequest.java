package Requests;

/**
 * RegisterRequest object that has username, password, and email stored in it
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password= password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
