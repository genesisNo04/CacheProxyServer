package com.example.CacheServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class CacheResponse {
    private int status;
    private byte[] body;
    private Map<String, String> headers;
}
