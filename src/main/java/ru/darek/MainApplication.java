package ru.darek;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainApplication {
    // Домашнее задание:
    // - Добавить логирование
    // - Добавить обработку запросов в параллельных потоках
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
      //  HttpServer server = new HttpServer(8189);
        HttpServer server = new HttpServer(Integer.parseInt((String)System.getProperties().getOrDefault("port", "8189")));
        server.start();


    }
}