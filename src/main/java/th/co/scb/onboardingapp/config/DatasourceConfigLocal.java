package th.co.scb.onboardingapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Profile({"local", "test"})
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class DatasourceConfigLocal {

    @Value("${postgres.datasource.url}")
    String postgresEndpoint;

    @Value("${postgres.datasource.username}")
    String postgresUsername;

    @Value("${postgres.datasource.password}")
    String postgresPassword;

    @Value("${postgres.datasource.schema}")
    String postgresSchema;

    @Primary
    @Bean(name = "postgresDatasource")
    public DataSource postgresDatasource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresEndpoint);
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername(postgresUsername);
        config.setPassword(postgresPassword);
        config.setSchema(postgresSchema);
        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "postgresJdbcTemplate")
    public NamedParameterJdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
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

    @Primary
    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("postgresDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
