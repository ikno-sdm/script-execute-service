package com.ikno.ikdata.external;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessBuilderConfig {

    @Bean
    public ProcessBuilder processBuilder() {
        return new ProcessBuilder();
    }
}