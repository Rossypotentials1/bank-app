package com.rossypotentials.bankApplication.infrastructure.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    private  final String CLOUD_NAME = "dsj1tatqo";
    private  final String API_KEY = "193691527386269";
    private  final String API_SECRET = "Vhx-AaO7TqVIjaTGZzoQOcpOA9A";

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME );
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);

        return new Cloudinary(config);
    }

}
