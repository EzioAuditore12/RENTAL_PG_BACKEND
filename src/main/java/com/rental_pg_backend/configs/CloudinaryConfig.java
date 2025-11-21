package com.rental_pg_backend.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    Cloudinary getCloudinary() {
        Map<String, Object> map = new HashMap<>();
        map.put("cloud_name", cloudName);
        map.put("api_key", apiKey);
        map.put("api_secret", apiSecret);
        map.put("secure", true);
        return new Cloudinary(map);
    }
}
