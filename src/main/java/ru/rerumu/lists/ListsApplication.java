package ru.rerumu.lists;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class ListsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListsApplication.class, args);
    }
}
