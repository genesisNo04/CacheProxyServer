package com.example.CacheServer.Controller;

import com.example.CacheServer.CacheStore.CacheStore;
import com.example.CacheServer.DTO.CacheResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RestController
public class ProxyController {

    private final WebClient client = WebClient.builder().build();

    @Value("${origin}")
    private String origin;

    @GetMapping("/**")
    public ResponseEntity<?> handleGet(HttpServletRequest request) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String fullPath = (query == null) ? path : path + "?" + query;

        CacheResponse cached = CacheStore.get(fullPath);

        if (cached != null) {
            return ResponseEntity
                    .status(cached.getStatus())
                    .headers(headers -> cached.getHeaders().forEach(headers::add))
                    .header("X-Cache", "HIT")
                    .body(cached.getBody());
        }

        String url = origin + fullPath;

        var response = client.get().uri(url)
                .exchangeToMono(clientResp ->
                        clientResp.bodyToMono(byte[].class)
                                .map(body -> Map.of(
                                        "status", clientResp.statusCode().value(),
                                        "headers", clientResp.headers().asHttpHeaders(),
                                        "body", body
                                ))).block();

        int status = (int) response.get("status");
        HttpHeaders originHeaders = (HttpHeaders) response.get("headers");
        byte[] body = (byte[]) response.get("body");

        Map<String, String> simpleHeaders = originHeaders.toSingleValueMap();

        CacheStore.put(fullPath, new CacheResponse(status, body, simpleHeaders));

        return ResponseEntity.status(status).headers(headers -> simpleHeaders.forEach(headers::add))
                .header("X-Cache", "MISS").body(body);
    }
}
