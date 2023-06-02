package main.java.wavemark.controller;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    
    private static final String url = "jdbc:sqlite:Esl.db";
    private static final String user = "wavemark";
    private static final String password = "wavemark";
    
    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the SQLite DB successfully.");
        
        return conn;
    }
}
