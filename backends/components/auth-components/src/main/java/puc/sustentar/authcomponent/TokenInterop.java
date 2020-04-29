package puc.sustentar.authcomponent;

import java.security.SecureRandom;

public interface TokenInterop  {

    void sessionTokensPut(String token,  SessionTokenHolder sessionTokenHolder);

    String createSessionToken(String email) ;
}
