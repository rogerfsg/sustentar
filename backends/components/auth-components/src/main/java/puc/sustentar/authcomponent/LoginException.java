package puc.sustentar.authcomponent;

public class LoginException extends RuntimeException {
    public LoginException(Exception ex){
        super(ex);
    }
}
