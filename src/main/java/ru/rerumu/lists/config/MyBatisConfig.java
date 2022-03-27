package ru.rerumu.lists.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.mappers.AuthorMapper;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.SeriesMapper;
import ru.rerumu.lists.mappers.TitleMapper;

import java.io.InputStream;

@Configuration
@MapperScan("ru.rerumu.lists.mappers")
public class MyBatisConfig {
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        return new SqlSessionFactoryBuilder().build(inputStream);
//    }
//    @Bean
//    public MapperFactoryBean<AuthorMapper> authorMapper() throws Exception {
//        MapperFactoryBean<AuthorMapper> factoryBean = new MapperFactoryBean<>(AuthorMapper.class);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory());
//        return factoryBean;
//    }
//
//    @Bean
//    public MapperFactoryBean<BookMapper> bookMapper() throws Exception {
//        MapperFactoryBean<BookMapper> factoryBean = new MapperFactoryBean<>(BookMapper.class);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory());
//        return factoryBean;
//    }
//
//    @Bean
//    public MapperFactoryBean<SeriesMapper> seriesMapper() throws Exception {
//        MapperFactoryBean<SeriesMapper> factoryBean = new MapperFactoryBean<>(SeriesMapper.class);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory());
//        return factoryBean;
//    }
//
//    @Bean
//    public MapperFactoryBean<TitleMapper> titleMapper() throws Exception {
//        MapperFactoryBean<TitleMapper> factoryBean = new MapperFactoryBean<>(TitleMapper.class);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory());
//        return factoryBean;
//    }
}
