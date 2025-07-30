package io.github.cursodsousa.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    //Use @Value quando são poucas propriedades isoladas.
    @Value("${spring.datasource.url}")  // Injecao individual das propriedades do application.yml
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driverClassName}")
    String driver;



    //@Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);

        return ds;
    }

    @Bean
    public DataSource hikariDataSource(){
        // Ja vem por padrao com Spring Data JPA
        HikariConfig config = new HikariConfig();

        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(10 );  // As vezes aumentando o POOL de conexoes, pode resolver o sistema lento (conexoes liberadas max)
        config.setMinimumIdle(1); // Tamanho inicial do pool
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000);  // 10 minutos em milissegundos
        config.setConnectionTimeout(100000);  // Tempo para conseguir outra conexao
        config.setConnectionTestQuery("select 1");  // Query de teste

        return new HikariDataSource(config);
    }
}
