package puc.sustentar.stores;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(Exception ex){
        super(ex);
    }
    public WrongPasswordException(){
    }
}
