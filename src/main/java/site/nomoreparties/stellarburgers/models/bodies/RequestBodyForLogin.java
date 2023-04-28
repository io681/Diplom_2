package site.nomoreparties.stellarburgers.models.bodies;

public class RequestBodyForLogin {
    private  String email;
    private  String password;

    public RequestBodyForLogin (String email, String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
