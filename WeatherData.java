package com.codtech.WeatherData;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class WeatherData {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        // Serve HTML
        server.createContext("/", exchange -> {
            byte[] response = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("webindex.html"));
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        // Serve JS
        server.createContext("/webjs.js", exchange -> {
            byte[] response = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("webjs.js"));
            exchange.getResponseHeaders().set("Content-Type", "application/javascript");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        // Serve CSS
        server.createContext("/webstyle.css", exchange -> {
            byte[] response = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("webstyle.css"));
            exchange.getResponseHeaders().set("Content-Type", "text/css");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        // API Endpoint
        server.createContext("/weather", exchange -> {
            String apiKey = "1519f4f566699b38a5f6233f39fedaa9";
            String city = "Mumbai";
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

            URL url = new URL(urlStr);
            //URL url=java.net.URI.create(urlStr).toURL();;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(result.toString());

            JSONObject main = json.getJSONObject("main");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");

            JSONObject responseJson = new JSONObject();
            responseJson.put("temperature", temp);
            responseJson.put("humidity", humidity);
            responseJson.put("description", description);

            byte[] response = responseJson.toString().getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://localhost:9090");
    }
}

