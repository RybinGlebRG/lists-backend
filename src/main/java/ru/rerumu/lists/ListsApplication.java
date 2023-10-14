package ru.rerumu.lists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListsApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ListsApplication.class, args);
        } catch (Exception e){
            System.exit(1);
        }
    }
}
