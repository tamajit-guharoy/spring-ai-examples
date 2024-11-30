package com.example.spring_ai_examples;

import com.example.spring_ai_examples.service.MockWeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@SpringBootApplication
public class SpringAiExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiExamplesApplication.class, args);
	}

	@Bean
	@Description("Get the weather in location") // function description
	public MockWeatherService currentWeather() {
		return new MockWeatherService();
	}
}
