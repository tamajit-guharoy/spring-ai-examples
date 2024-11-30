package com.example.spring_ai_examples.service;

import java.util.function.Function;

public class MockWeatherService implements Function<MockWeatherService.Request, MockWeatherService.Response> {

    public record Request(String location) {
    }

    public record Response(double temp) {
    }

    public Response apply(Request request) {
        Response response = null;
        switch (request.location.toLowerCase()) {
            case "new delhi":
                response = new Response(30.0);
                break;
            case "london":
                response = new Response(10.0);
                break;
            case "paris":
                response = new Response(20.0);
                break;
            default:
                response = new Response(0.0);
        }
        return response;

    }
}
