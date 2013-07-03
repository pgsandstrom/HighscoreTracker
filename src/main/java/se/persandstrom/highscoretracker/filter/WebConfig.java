package se.persandstrom.highscoretracker.filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "se.persandstrom.highscoretracker")
public class WebConfig extends WebMvcConfigurerAdapter {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //this line is needed so for example js-files can find other js-files:
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/", "/",
                "classpath:/META-INF/web-resources/");

        registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/");
    }
}