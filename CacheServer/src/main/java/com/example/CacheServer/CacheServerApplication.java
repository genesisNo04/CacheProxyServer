package com.example.CacheServer;

import com.example.CacheServer.CacheStore.CacheStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CacheServerApplication implements ApplicationRunner {

	private static String origin;
	private static Integer port;
	private static boolean clearCache = false;

	public static void main(String[] args) {

		if (args.length == 0 || !args[0].equals("caching-proxy")) {
			throw new IllegalArgumentException("No command with name: " + (args.length > 0 ? args[0] : "null"));
		}

		for (int i = 1; i < args.length; i++) {
			switch (args[i]) {
				case "--port":
					port = Integer.parseInt(args[++i]);
					System.setProperty("server.port", String.valueOf(port)); // set port BEFORE Spring runs
					break;
				case "--origin":
					origin = args[++i];
					break;
				case "--clear-cache":
					clearCache = true;
					break;
			}
		}

		SpringApplication.run(CacheServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (clearCache) {
			CacheStore.clear();
			System.out.println("Cache cleared.");
			return;
		}

		if (origin == null || origin.isEmpty() || port == null) {
			throw new IllegalArgumentException("Usage: --port=<number> --origin=<url>");
		}

		System.out.println("Starting caching proxy on port " + port);
		System.out.println("Forwarding requests to " + origin);
	}

	public static String getOrigin() {
		return origin;
	}
}
