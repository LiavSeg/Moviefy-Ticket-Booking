package moviefyPackge.moviefy.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.sql.*;

/**
 * Database bootstrapper that guarantees the target DB user (ROLE) and database exist
 * before Spring/Hibernate start using the DataSource.

 * Flow:
 * 1) Connect to the maintenance DB ("postgres") using an admin account (superuser or CREATEROLE/CREATEDB).
 * 2) Ensure the application ROLE (username) exists with a password (CREATE ROLE ... LOGIN PASSWORD ...).
 * 3) Ensure the target database exists; if missing, CREATE DATABASE ... OWNER <role>.
 * 4) Return a HikariDataSource that points to the target DB using the *application* credentials.

 * Notes:
 * - The admin account must have enough privileges to CREATE ROLE and CREATE DATABASE.
 * - Identifiers are quoted and passwords are sanitized to avoid SQL issues with special characters.
 * - After this bean is created, Hibernate can safely apply ddl-auto as usual.
 */
@Slf4j
@Configuration
public class DatabaseProvisioningConfig {

    // Application (target) datasource; this is what Hibernate will use:
    @Value("${spring.datasource.url}")
    private String appJdbcUrl; // e.g. jdbc:postgresql://localhost:5432/
    @Value("${spring.datasource.username}")
    private String appUser;
    @Value("${spring.datasource.password}")
    private String appPass;

    // Admin connection to "postgres" used ONLY for provisioning
    @Value("${spring.datasource.admin.url:}")
    private String adminJdbcUrl; // e.g. jdbc:postgresql://localhost:5432/postgres
    @Value("${spring.datasource.admin.username:${spring.datasource.username}}")
    private String adminUser;
    @Value("${spring.datasource.admin.password:${spring.datasource.password}}")
    private String adminPass;

    @Bean
    public HikariDataSource dataSource() {
        ParsedTarget t = parsePostgresUrl(appJdbcUrl);

        // Build default admin URL if not provided explicitly
        String adminUrl = (adminJdbcUrl == null || adminJdbcUrl.isBlank())
                ? String.format("jdbc:postgresql://%s:%d/postgres", t.host, t.port)
                : adminJdbcUrl;

        // 1) Ensure ROLE (user) exists
        ensureRoleExists(adminUrl, adminUser, adminPass, appUser, appPass);

        // 2) Ensure DB exists and owned by the ROLE
        ensureDatabaseExists(adminUrl, adminUser, adminPass, t.dbName, appUser);

        // 3) Return the real application DataSource (Hibernate will use this)
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(appJdbcUrl);
        ds.setUsername(appUser);
        ds.setPassword(appPass);
        ds.setPoolName("moviefy-pool");
        ds.setMaximumPoolSize(10);
        return ds;
    }

    private void ensureRoleExists(String adminJdbc, String aUser, String aPass,
                                  String roleName, String rolePassword) {
        final String check = "SELECT 1 FROM pg_roles WHERE rolname = ?";
        try (Connection c = DriverManager.getConnection(adminJdbc, aUser, aPass);
             PreparedStatement ps = c.prepareStatement(check)) {
            ps.setString(1, roleName);
            if (ps.executeQuery().next()) {
                log.info("Role '{}' already exists.", roleName);
                return;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed checking role existence", e);
        }

        log.warn("Role '{}' not found. Creating…", roleName);
        // quote identifier and escape password safely
        String quotedRole = quoteIdent(roleName);
        String escapedPass = rolePassword.replace("'", "''");
        // Give CREATEDB so the role can later own DB objects if needed; remove if undesired
        String ddl = "CREATE ROLE " + quotedRole + " WITH LOGIN PASSWORD '" + escapedPass + "' NOCREATEROLE NOSUPERUSER CREATEDB";
        try (Connection c = DriverManager.getConnection(adminJdbc, aUser, aPass);
             Statement st = c.createStatement()) {
            st.executeUpdate(ddl);
            log.info("Role '{}' created.", roleName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed creating role " + roleName, e);
        }
    }

    private void ensureDatabaseExists(String adminJdbc, String aUser, String aPass,
                                      String dbName, String ownerRole) {
        final String checkDb = "SELECT 1 FROM pg_database WHERE datname = ?";
        try (Connection c = DriverManager.getConnection(adminJdbc, aUser, aPass);
             PreparedStatement ps = c.prepareStatement(checkDb)) {
            ps.setString(1, dbName);
            if (ps.executeQuery().next()) {
                log.info("Database '{}' already exists.", dbName);
                return;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed checking database existence", e);
        }

        log.warn("Database '{}' not found. Creating…", dbName);
        String qDb = quoteIdent(dbName);
        String qOwner = quoteIdent(ownerRole);
        String ddl = "CREATE DATABASE " + qDb + " WITH OWNER " + qOwner + " ENCODING 'UTF8' TEMPLATE template0";
        try (Connection c = DriverManager.getConnection(adminJdbc, aUser, aPass);
             Statement st = c.createStatement()) {
            st.executeUpdate(ddl);
            log.info("Database '{}' created with owner '{}'.", dbName, ownerRole);
        } catch (Exception e) {
            throw new IllegalStateException("Failed creating database " + dbName, e);
        }
    }

    private static String quoteIdent(String ident) {
        // Quote PostgreSQL identifier: "na""me" to handle special chars and dashes
        return "\"" + ident.replace("\"", "\"\"") + "\"";
    }

    private ParsedTarget parsePostgresUrl(String jdbcUrl) {
        // expects jdbc:postgresql://host:port/db?...
        String raw = jdbcUrl.substring("jdbc:".length()); // -> postgresql://...
        URI uri = URI.create(raw);
        String host = uri.getHost();
        int port = (uri.getPort() == -1) ? 5432 : uri.getPort();
        String path = uri.getPath(); // "/dbname"
        String db = (path != null && path.length() > 1) ? path.substring(1) : "";
        if (host == null || db.isBlank())
            throw new IllegalArgumentException("Invalid JDBC URL for PostgreSQL: " + jdbcUrl);
        return new ParsedTarget(host, port, db);
    }

    private record ParsedTarget(String host, int port, String dbName) {}
    
}
