package com.example.demoapi.spcs_helpers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

public class SnowflakeConnection {    
    public static Connection snowflakeConnection() throws Exception {
        Map<String,String> env = System.getenv();

        Properties props = new Properties();        
        String url = "jdbc:snowflake://" + env.getOrDefault("SNOWFLAKE_ACCOUNT", null) + ".snowflakecomputing.com/";
        if (Files.exists(Paths.get("/snowflake/session/token"))) {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", env.get("SNOWFLAKE_ACCOUNT"));
            props.put("authenticator", "OAUTH");
            props.put("token", new String(Files.readAllBytes(Paths.get("/snowflake/session/token"))));
            props.put("warehouse", env.getOrDefault("SNOWFLAKE_WAREHOUSE", null));
            props.put("db", env.get("SNOWFLAKE_DATABASE"));
            props.put("schema", env.get("SNOWFLAKE_SCHEMA"));
            url = "jdbc:snowflake://" + env.get("SNOWFLAKE_HOST") + ":" + env.get("SNOWFLAKE_PORT");
        }
        else {
            props.put("CLIENT_SESSION_KEEP_ALIVE", true);
            props.put("account", env.getOrDefault("SNOWFLAKE_ACCOUNT", null));
            props.put("user", env.getOrDefault("SNOWFLAKE_USER", null));
            props.put("password", env.getOrDefault("SNOWFLAKE_PASSWORD", null));
            props.put("warehouse", env.getOrDefault("SNOWFLAKE_WAREHOUSE", null));
            props.put("db", env.getOrDefault("SNOWFLAKE_DATABASE", null));
            props.put("schema", env.getOrDefault("SNOWFLAKE_SCHEMA", null));
        }
        return DriverManager.getConnection(url, props);
    }
}

