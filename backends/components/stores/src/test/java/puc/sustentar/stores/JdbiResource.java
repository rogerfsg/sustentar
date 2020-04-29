package puc.sustentar.stores;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.rules.ExternalResource;

import static puc.sustentar.entities.DatabaseBootstrap.bootJdbiWithH2;


public class JdbiResource extends ExternalResource {
    private static final Jdbi jdbi = bootJdbiWithH2();

    private Handle handle;

    @Override protected void before() {
        handle = jdbi.open();
        handle.begin();
    }

    @Override protected void after() {
        try {
            handle.rollback();
        } finally {
            handle.close();
        }
    }

    public Handle getHandle() {
        return handle;
    }
}
