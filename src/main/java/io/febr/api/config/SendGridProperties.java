package io.febr.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sendgrid")
public record SendGridProperties(String apiKey, String fromEmail) {
}