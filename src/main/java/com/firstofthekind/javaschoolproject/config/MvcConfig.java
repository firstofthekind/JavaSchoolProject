package com.firstofthekind.javaschoolproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/tariffs").setViewName("tariffs");
        registry.addViewController("/tariffs?created").setViewName("tariffs?created");
        registry.addViewController("/cart").setViewName("cart");
        registry.addViewController("/error").setViewName("main");

    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/assets/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry
                .addResourceHandler("/static/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry
                .addResourceHandler("/assets/**", "assets/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/static/**", "static/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("assets/")
                .addResourceLocations("classpath:assets/");

    }


}
