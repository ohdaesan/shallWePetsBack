package com.ohdaesan.shallwepets.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        // entity와 DTO간의 변환을 용이하게 해주는 라이브러리
        return new ModelMapper();
    }
}
