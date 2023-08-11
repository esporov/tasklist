package com.example.tasklist;

import com.example.tasklist.repository.TaskRepository;
import com.example.tasklist.repository.UserRepository;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import com.example.tasklist.service.impl.AuthServiceImpl;
import com.example.tasklist.service.impl.TaskServiceImpl;
import com.example.tasklist.service.impl.UserServiceImpl;
import com.example.tasklist.service.props.JwtProperties;
import com.example.tasklist.web.security.JwtUserDetailsService;
import com.example.tasklist.web.security.JwtUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@org.springframework.boot.test.context.TestConfiguration
@RequiredArgsConstructor
public class TestConfiguration {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AuthenticationManager authenticationManager;

    @Bean
    @Primary
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        var jwtProperties = new JwtProperties();
        jwtProperties.setSecret("YWRzYXNkcXdkZGZnZHNmZGFkc2Rmc1xk");
        return jwtProperties;
    }

    @Bean
    public JwtUserDetailsService jwtUserDetailsService() {
        return new JwtUserDetailsService(userService());
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtProperties(),
                userService(),
                jwtUserDetailsService());
    }

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, bCryptPasswordEncoder());
    }

    @Bean
    @Primary
    public TaskService taskService() {
        return new TaskServiceImpl(taskRepository, userService());
    }



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
