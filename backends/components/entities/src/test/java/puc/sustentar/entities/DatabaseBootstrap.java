package puc.sustentar.entities;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jpa.JpaPlugin;

import javax.sql.DataSource;

public class DatabaseBootstrap {


//    public static Jdbi bootJdbiWithMySQL() {
//
//        String url = "jdbc:mysql://localhost:3306/digicash?user=root&password=root&useLegacyDatetimeCode=false";
//
//        MysqlDataSource jdbcDataSource = new MysqlDataSource();
//        jdbcDataSource.setUrl(url);
//
//        Jdbi jdbi = Jdbi.create(url);
//        jdbi.installPlugin(new JpaPlugin());
//
//        bootFlyway(jdbcDataSource);
//        return jdbi;
//    }

    public static Jdbi bootJdbiWithH2() {
        String url = "jdbc:h2:mem:foo;DB_CLOSE_DELAY=-1";
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl(url);

        Jdbi jdbi = Jdbi.create(url);
        jdbi.installPlugin(new JpaPlugin());

        bootFlyway(jdbcDataSource);

        return jdbi;
    }


    public static void bootFlyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("v");
        flyway.setSqlMigrationSeparator("_");
        flyway.setSqlMigrationSuffixes(".sql");
        String ddlLocaltion = "classpath:db/ddl/";
        String dummyLocaltion = "classpath:db/dummy/";
//        String allLocaltion = "classpath:db/all/";
//        String ddlManualFixLocaltion = "classpath:db/ddlManualFix/";
        flyway.setLocations( ddlLocaltion, dummyLocaltion);
        // Start the migration

        flyway.clean();
        flyway.migrate();
        flyway.validate();
        flyway.info();
    }
}
