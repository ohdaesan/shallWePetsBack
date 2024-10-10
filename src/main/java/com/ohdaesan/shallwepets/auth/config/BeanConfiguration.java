package com.ohdaesan.shallwepets.auth.config;

import com.ohdaesan.shallwepets.post.domain.entity.Status;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();


        // enum Status를 String으로 바꿔서 리턴
        Converter<Status, String> statusToStringConverter = new Converter<Status, String>() {
            public String convert(MappingContext<Status, String> context) {
                return context.getSource() != null ? context.getSource().name() : null;
            }
        };

        modelMapper.addConverter(statusToStringConverter);

        // ModelMapper의 매핑 전략 적용 - private 필드 매핑
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
