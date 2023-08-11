package com.example.tasklist;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15.1");
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariDataSource.setUsername(postgreSQLContainer.getUsername());
        hikariDataSource.setPassword(postgreSQLContainer.getPassword());

        return hikariDataSource;
    }
}
