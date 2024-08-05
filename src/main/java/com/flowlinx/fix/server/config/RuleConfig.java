package com.flowlinx.fix.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class RuleConfig {
    @Bean
    public List<Map<String, Object>> loadRules() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                getClass().getResourceAsStream("/newRules.json"),
                new TypeReference<List<Map<String, Object>>>() {}
        );
    }
}
