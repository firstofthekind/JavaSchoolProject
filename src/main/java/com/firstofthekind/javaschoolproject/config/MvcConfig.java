package com.firstofthekind.javaschoolproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");

    }
}
