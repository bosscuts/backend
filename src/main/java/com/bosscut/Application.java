package com.bosscut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, TaskExecutionProperties.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @PostConstruct
//    public void init() {
//
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//
//        System.out.println("Date in UTC: " + new Date());
//        System.out.println("Date in UTC: " + ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now().plusHours(7)));
//    }
//

}
