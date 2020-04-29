package puc.sustentar.authcomponent;

import com.google.gson.annotations.Expose;

public class LoggedIn {

    @Expose
    private String result = "logged in";

    @Expose
    private String token;

    public LoggedIn() {
    }

    public LoggedIn(String token) {
        this.token = token;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
