package com.tx.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Demo class
 *
 * @author tx
 * @ComponentScan("com.tx.springboot.*")用来扫描装配好的Bean
 * @date 2018/10/29
 */
@SpringBootApplication
@ComponentScan("com.tx.springboot.*")
@MapperScan("dao")
public class SpringBootSsm01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSsm01Application.class, args);
    }
}
