package com.core.router;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.core.dao.UserDAO;

public class Router {
    private final HttpServer server;
    private final UserDAO userDAO;

    public Router() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        this.userDAO = new UserDAO();
        setupRoutes();
    }

    private void setupRoutes() {
        server.createContext("/users", exchange -> {
            try {
                String response = userDAO.list().toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}