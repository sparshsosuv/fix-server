package com.flowlinx.fix.server.resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowlinx.fix.server.domain.Rule;
import com.flowlinx.fix.server.representation.FixSessionRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/rules")
public class NormalizationRuleResource {

    @GetMapping
    public ResponseEntity<Rule> getRules() {
        System.out.println("GET RULES....");
        ObjectMapper objectMapper = new ObjectMapper();
        Rule rule = null;
//        try {
//            File file = new File("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/newRules.json");
//            rule = objectMapper.readValue(file, Rule.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
        return ResponseEntity.ok(rule);
    }

//    @PostMapping
//    public ResponseEntity<Rule> setRule() {
//        return ResponseEntity.ok(null);
//    }
}
