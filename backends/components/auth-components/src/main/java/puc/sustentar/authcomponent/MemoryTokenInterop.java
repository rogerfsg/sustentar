package puc.sustentar.authcomponent;

import java.util.HashMap;

import static puc.sustentar.common.CoreTokenChecker.randomToken;

public class MemoryTokenInterop implements  TokenInterop{
    HashMap<String, SessionTokenHolder> hash = new HashMap<>();

    @Override
    public void sessionTokensPut(String token, SessionTokenHolder sessionTokenHolder) {
        hash.put(token, sessionTokenHolder);
    }

    @Override
    public String createSessionToken(String email) {
        String token = randomToken(28);
        sessionTokensPut(token, new SessionTokenHolder(token, email));

        return token;
    }
}
