package puc.sustentar.auth;

import org.jooby.Jooby;
import puc.sustentar.authcomponent.MemoryTokenInterop;
import puc.sustentar.authcomponent.TokenInterop;

import static puc.sustentar.auth.AuthApp.AUTH_APP_SETUP;

public class AuthMain {


  public static void main(final String[] args) {
    TokenInterop memoryTokenInterop = new MemoryTokenInterop();
    Jooby.run(() -> new AuthApp(memoryTokenInterop, AUTH_APP_SETUP), args);

  }

}
