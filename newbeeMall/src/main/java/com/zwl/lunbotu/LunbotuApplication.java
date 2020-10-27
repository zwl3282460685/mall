package com.zwl.lunbotu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zwl.lunbotu.dao") //添加 @Mapper 注解
public class LunbotuApplication {
    public static void main(String[] args) {
        SpringApplication.run(LunbotuApplication.class, args);
    }
}
