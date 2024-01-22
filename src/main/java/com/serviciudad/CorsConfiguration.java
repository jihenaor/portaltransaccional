package com.serviciudad;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH")
                .allowedOrigins("*")
                .allowedHeaders("*");
        /*
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedOrigins("https://serviciudadpse.com",
                        "https://serviciudad.gov.co",
                		"http://localhost:4200",
                        "*");

         */
    }
}
