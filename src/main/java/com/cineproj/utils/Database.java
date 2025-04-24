package com.cineproj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://db.ipyxrmnqjulutxucexmg.supabase.co:5432/postgres?user=postgres&password=3RrcNuGDKWxZrBJy";

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
