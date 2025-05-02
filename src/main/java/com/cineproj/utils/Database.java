package com.cineproj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    
    private static final String URL;

    static {
        URL = System.getenv("DB_URL");
        if (URL == null) {
            throw new RuntimeException("DB_URL is not set in .env");
        }
    }

    static {
        try {
        	Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
