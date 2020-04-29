package puc.sustentar.authcomponent;

import java.io.Serializable;

public class SessionTokenHolder implements Serializable {

    private String token;
    private final String email;
    private long created;
    private long lastUsed;

    public SessionTokenHolder(String token, String email) {
        this.token = token;
        this.email = email;
        this.created = System.currentTimeMillis();
        updateLastUsedMillis();
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public boolean expired(int duration) {
        return (System.currentTimeMillis() - lastUsed) > duration;
    }

    public void updateLastUsedMillis() {
        lastUsed = System.currentTimeMillis();
    }
}
