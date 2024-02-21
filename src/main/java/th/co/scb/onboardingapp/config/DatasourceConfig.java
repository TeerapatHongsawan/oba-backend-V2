package th.co.scb.onboardingapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Profile({"dev", "qa", "qaNext", "sit", "uat", "ps", "pt", "prod"})
@Configuration
public class DatasourceConfig {
    @Value("${postgres.datasource.url}")
    String postgresEndpoint;
    @Value("${postgres.datasource.username}")
    String postgresUsername;
    @Value("${postgres.datasource.password}")
    String postgresPassword;
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

    @Value("${postgres.datasource.schema}")
    String postgresSchema;

    @Bean
    @Primary
    @ConfigurationProperties("postgres.datasource")
    public DataSourceProperties postgresProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "postgresDatasource")
    @Primary
    public DataSource postgresDatasource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresEndpoint);
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername(postgresUsername);
        config.setPassword(postgresPassword);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setValidationTimeout(validationTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setInitializationFailTimeout(initializationFailTimeout);

        return new HikariDataSource(config);
    }

    @Bean(name = "postgresJdbcTemplate")
    @Primary
    public NamedParameterJdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "postgresTransactionManager")
    @Primary
    public PlatformTransactionManager postgresTransactionManager(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postgresDatasource") DataSource dataSource
    ) {

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.default_schema", postgresSchema);

        return builder
                .dataSource(dataSource)
                .packages("th.co.scb.onboardingapp.model.entity")
                .persistenceUnit("onboardingapp")
                .properties(properties)
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
