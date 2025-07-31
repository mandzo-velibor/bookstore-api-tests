package com.bookstore.api.config;

import lombok.Data;

@Data
public class ApiConfiguration {
    private final String baseUrl;
    private final String booksEndpoint;
    private final String authorsEndpoint;

    public ApiConfiguration(String baseUrl, String booksEndpoint, String authorsEndpoint) {
        this.baseUrl = baseUrl;
        this.booksEndpoint = booksEndpoint;
        this.authorsEndpoint = authorsEndpoint;
    }
}