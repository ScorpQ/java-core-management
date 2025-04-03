package com.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;

import com.core.utils.Color;

public class SingleDBConnection {

    // Fields
    private static final String URL = "jdbc:h2:./h2db/user_management";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";


    // Composition
    private static Connection connection;
    private static SingleDBConnection instance;

    public SingleDBConnection() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(Color.GREEN + "✅ Veritabanı bağlantısı başarılı." + Color.RESET);
            
            // H2DB
            H2DbStarting();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Color.RED + "❌ Veritabanı bağlantısı başarısız!" + Color.RESET);
        }
    }

    // H2DB
    // H2 Web Konsolunu başlatmak için
    private void H2DbStarting() {
        try {
            Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Web Console is running at: http://localhost:8082");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Singleton instance
    public static synchronized SingleDBConnection getInstance() {
        if (instance == null) {
            instance = new SingleDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
