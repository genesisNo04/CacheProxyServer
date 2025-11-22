package com.example.CacheServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CacheServerApplication implements ApplicationRunner {

	@Value("${origin:}")
	private String origin;

	@Value("${port:}")
	private Integer port;

	@Value("${clear-cache:false}")
	private boolean clearCache;

	public static void main(String[] args) {
		SpringApplication.run(CacheServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (clearCache) {

		}
	}
}
