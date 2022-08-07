package ru.rerumu.lists.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("ru.rerumu.lists.mappers")
public class MyBatisConfig {
}
