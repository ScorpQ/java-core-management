package com.core.router;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.core.dao.UserDAO;
import com.core.dto.UserDTO;

public class MainRouter {
    private final HttpServer server;
    private final UserDAO userDAO;

    public MainRouter() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        this.userDAO = new UserDAO();
        setupRoutes();
    }

    private void setupRoutes() {
        server.createContext("/login", this::handleLogin);
        server.createContext("/register", this::handleRegister);
    }

    /////////////////////////////// LOGIN ROUTER ///////////////////////////////
    private void handleLogin(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // Request body'yi oku
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    
                // JSON'ı parse et (basit bir örnek)
                String email = extractValue(requestBody, "email");
                String password = extractValue(requestBody, "password");
                    
                // Login işlemi
                UserDTO user = userDAO.loginUser(email, password);

                String response = user != null ? "Giriş başarılı" : "Giriş başarısız";

                exchange.sendResponseHeaders(200, response.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {    
                    os.write(response.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
    } 



    /////////////////////////////// REGISTER ROUTER ///////////////////////////////
    private void handleRegister(HttpExchange exchange) throws IOException {
        try {
            String response = userDAO.list().toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }


    /////////////////////////////// ADMIN ROUTER ///////////////////////////////



    /////////////////////////////// Common METHODS ///////////////////////////////    
    // JSON string'den değer çıkarma (basit bir örnek)
    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey) + searchKey.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
        return json.substring(startIndex, endIndex).trim().replace("\"", "");
    }

    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.println("Server başlatıldı - Port: 8080");
    }

}
