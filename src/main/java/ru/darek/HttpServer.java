package ru.darek;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    OutputStream o = socket.getOutputStream();
                    byte[] buffer = new byte[8192];
                    int n = socket.getInputStream().read(buffer);
                    String rawRequest = new String(buffer, 0, n);
                    executorService.execute(() -> {
                        HttpRequest httpRequest = new HttpRequest(rawRequest);
                        try {
                            dispatcher.execute(httpRequest, o);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    // dispatcher.execute(httpRequest, socket.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}