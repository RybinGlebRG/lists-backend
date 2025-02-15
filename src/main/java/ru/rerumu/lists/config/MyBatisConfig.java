package ru.rerumu.lists.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages={"ru.rerumu.lists.mappers", "ru.rerumu.lists.dao"}, annotationClass = Mapper.class)
public class MyBatisConfig {
}
