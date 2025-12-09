package com.dlz.test.framework.db;

import com.dlz.framework.db.service.ICommPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class Starter {

    @Autowired
    public ICommPlusService cs;
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}