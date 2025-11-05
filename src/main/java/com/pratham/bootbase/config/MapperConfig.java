package com.pratham.bootbase.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapperBean(){
        ModelMapper mapper = new ModelMapper();
        Converter<String, String> trimConverter = new AbstractConverter<>() {
            @Override
            protected String convert(String source) {
                return source == null ? null : source.trim();
            }
        };
        mapper.addConverter(trimConverter);
        return mapper;
    }
}
