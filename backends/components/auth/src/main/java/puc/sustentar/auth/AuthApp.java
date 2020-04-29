package puc.sustentar.auth;

import org.jdbi.v3.jpa.JpaPlugin;
import org.jooby.Jooby;
import org.jooby.Request;
import org.jooby.RequestLogger;
import org.jooby.Response;
import org.jooby.flyway.Flywaydb;
import org.jooby.hbm.Hbm;
import org.jooby.jdbc.Jdbc;
import org.jooby.jdbi.Jdbi3;
import org.jooby.json.Gzon;
import puc.sustentar.authcomponent.AuthComponent;
import puc.sustentar.authcomponent.LoggedIn;
import puc.sustentar.authcomponent.TokenInterop;
import puc.sustentar.common.JsonUtil;
import puc.sustentar.stores.MemberJdbiStore;
import puc.sustentar.stores.MemberStore;

import java.util.function.Consumer;


public class AuthApp extends Jooby {

    public static final String APP_NAME = "AuthApp";

    private final TokenInterop tokenInterop;

    /**
     * Configure Auth app in standalone mode:
     */
    public static final Consumer<Jooby> AUTH_APP_SETUP = app -> {
        app.use("*", new RequestLogger());
        app.use(new Gzon().doWith(JsonUtil::gsonBuilderFactory));
        app.use(new Jdbc());
        app.use(new Flywaydb());
        app.use(new Jdbi3().doWith(jdbi -> jdbi.installPlugin(new JpaPlugin())).transactionPerRequest());
        app.use(new Hbm().scan("puc.sustentar.entities"));
        app.use("*", Hbm.openSessionInView());
        app.bind(MemberStore.class, MemberJdbiStore.class);
    };


    public AuthApp(final TokenInterop tokenInterop) {
        this(tokenInterop, null);
    }

    public AuthApp(
            final TokenInterop tokenInterop,
            Consumer<Jooby> configurer) {
        this.tokenInterop = tokenInterop;

        if (configurer != null) {
            configurer.accept(this);
        }

        path("/auth", () -> {
            post("/login", this::login);
        });


    }

    private void login(Request req, Response rsp) throws Throwable {

        AuthComponent authComponent = require(AuthComponent.class);
        Login login = req.body(Login.class);

        LoggedIn loggedIn = authComponent.login(login.getEmail(), login.getPassword(), tokenInterop);
        rsp.status(200);
        rsp.type("application/json");
        rsp.send(loggedIn);
    }
}
