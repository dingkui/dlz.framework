package com.dlz.test.framework.db;

//import com.dlz.framework.db.DbInfo;

import com.dlz.framework.db.holder.DBHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
    @PostConstruct
    void init(){
        DBHolder.getDao();
//        String sql = DbInfo.getSql("key.setting.getSettings");
//        System.out.println(sql);
    }
}