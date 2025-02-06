//package com.flowlinx.fix.server.service;
//
//import com.flowlinx.fix.server.domain.Rule;
//import com.flowlinx.fix.server.repository.RuleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class RuleService {
//
//    @Autowired
//    private RuleRepository ruleRepository;
//
//    public List<Map<String, Object>> getAllRules() {
//        List<Rule> rules = ruleRepository.findAll();
//        return rules.stream()
//                .map(this::convertToMap)
//                .collect(Collectors.toList());
//    }
//
//    private Map<String, Object> convertToMap(Rule rule) {
//        return Map.of(
//                "id", rule.getId(),
//                "action", rule.getAction(),
//                "source", rule.getSource(),
//                "conditions", rule.getConditions()
//        );
//    }
//}



package com.flowlinx.fix.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowlinx.fix.server.domain.Rule;
import com.flowlinx.fix.server.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public List<Map<String, Object>> getAllRules() throws IOException {
        List<Rule> rules = ruleRepository.findAll();
        List<Map<String, Object>> rulesList = rules.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());

//        writeJsonToFile(rulesList);

        return rulesList;
    }

    private Map<String, Object> convertToMap(Rule rule) {
        return Map.of(
                "id", rule.getId(),
                "action", rule.getAction(),
                "source", rule.getSource(),
                "conditions", rule.getConditions(),
                "target", rule.getTarget() == null ? "" : rule.getTarget(),
                "value", rule.getValue() == null ? "" : rule.getValue()
        );
    }

    private void writeJsonToFile(List<Map<String, Object>> rulesList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // To pretty-print the JSON
        objectMapper.writeValue(new File("/opt/sosuv/repositories/sosuv-fix-server/src/main/resources/newRules.json"), rulesList);
    }
}
