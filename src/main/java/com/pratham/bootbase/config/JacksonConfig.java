package com.pratham.bootbase.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Support for java.time.* (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Serialize dates as ISO-8601 strings, not timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Ignore extra JSON properties not mapped to fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Exclude null fields from JSON output
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Use toString() for Enums instead of ordinal values
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        // Optional: pretty-print JSON (only useful for logs/dev)
        // mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
