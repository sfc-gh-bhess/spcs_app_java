package com.example.demoapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demoapi.spcs_helpers.SnowflakeConnection;

import java.sql.Connection;

@Configuration
public class DemoapiConfiguration {
    @Bean
    public Connection snowflakeConnection() throws Exception {
        return SnowflakeConnection.snowflakeConnection();
    }
}

