package th.co.scb.onboardingapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Profile({"local", "test", "dev", "qa", "qaNext", "sit", "uat", "ps", "pt", "prod"})
@Configuration
public class StartBizDatasourceConfig {
    @Value("${startbiz.datasource.url}")
    String startbizEndpoint;
    @Value("${startbiz.datasource.username}")
    String startbizUsername;
    @Value("${startbiz.datasource.password}")
    String startbizPassword;
    @Value("${spring.datasource.minimumIdle}")
    int minimumIdle;
    @Value("${spring.datasource.maximumPoolSize}")
    int maxPoolSize;
    @Value("${spring.datasource.idleTimeout}")
    int idleTimeout;
    @Value("${spring.datasource.connectionTimeout}")
    int connectionTimeout;
    @Value("${spring.datasource.validationTimeout}")
    int validationTimeout;
    @Value("${spring.datasource.maxLifetime}")
    int maxLifetime;
    @Value("${spring.datasource.initializationFailTimeout}")
    int initializationFailTimeout;

    @Bean
    @ConfigurationProperties("startbiz.datasource")
    public DataSourceProperties startbizProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "startbizDatasource")
    public DataSource startbizDatasource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(startbizEndpoint);
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername(startbizUsername);
        config.setPassword(startbizPassword);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setValidationTimeout(validationTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setInitializationFailTimeout(initializationFailTimeout);

        return new HikariDataSource(config);
    }

    @Bean(name = "startbizJdbcTemplate")
    public NamedParameterJdbcTemplate startbizJdbcTemplate(@Qualifier("startbizDatasource") final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "startbizTransactionManager")
    public PlatformTransactionManager startbizTransactionManager(@Qualifier("startbizDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
