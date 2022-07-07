package com.chumamhango.issuetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class, org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration.class})
@RestController
public class IssueTrackerApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IssueTrackerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(IssueTrackerApplication.class, args);
    }
}
