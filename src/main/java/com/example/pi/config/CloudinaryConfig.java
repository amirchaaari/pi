package com.example.pi.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duy16zpnl",
                "api_key", "465416742217448",
                "api_secret", "aMdyPWQLXXxyoelxrDQHC4wLEOo"));
    }
}
