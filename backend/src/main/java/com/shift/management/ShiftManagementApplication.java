package com.shift.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shift.management.mapper")
public class ShiftManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftManagementApplication.class, args);
    }
}
