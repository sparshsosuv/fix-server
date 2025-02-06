package com.flowlinx.fix.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowlinx.fix.server.domain.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickfix.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NormalizationService {

    private List<Map<String, Object>> rules;

    @Autowired
    private RuleService ruleService;

    public NormalizationService() {
//        loadRules();
    }

    public List<Map<String, Object>> getRules() throws IOException {
        return ruleService.getAllRules();
    }

    private void loadRules() {
        try {
//            log.info("Loading Normalization Rules...");
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonContent = new String(Files.readAllBytes(Paths.get("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/newRules.json")));
//            rules = objectMapper.readValue(jsonContent, new TypeReference<List<Map<String, Object>>>() {});


            rules = getRules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTagValue(Message fixMessage, int tag) {

        try {
            return fixMessage.getHeader().getString(tag);
        } catch (FieldNotFound e) {
            try {
                return fixMessage.getString(tag);
            } catch (FieldNotFound e1) {
                return null;
            }
        }
    }

//    public void setTagValue(Message fixMessage, int tag, String value) {
//        try {
//            if (fixMessage.isSetField(tag)) {
//                fixMessage.getField(new StringField(tag)).setValue(value);
//            } else {
//                fixMessage.setField(new StringField(tag, value));
//            }
//        } catch (FieldNotFound e) {
//            fixMessage.setField(new StringField(tag, value));
//        }
//    }

    public Message setTagValue(Message message, int tag, String value) throws IncorrectTagValue, FieldNotFound, InvalidMessage, ConfigError {
        String fixString = message.toString(), updatedFixString;
        String existingTagValue = getTagValue(message, tag);
        int existingTagIndex = fixString.indexOf(String.valueOf(tag) + '=');

        if (existingTagIndex != -1 && existingTagValue != null) {
            int tagValueStartIndex = fixString.indexOf('=', fixString.indexOf(String.valueOf(tag) + '=')) + 1;
            int tagValueEndIndex = fixString.indexOf('\u0001', fixString.indexOf(String.valueOf(tag) + '='));
            updatedFixString = fixString.substring(0, tagValueStartIndex) + value + fixString.substring(tagValueEndIndex);
            return new Message(updatedFixString, new DataDictionary("/opt/sosuv/repositories/sosuv-fix-server/src/main/resources/fix/FIX44.xml"), false);
        } else {
            int tag10Index = fixString.indexOf("10=");
            String newTagString = tag + "=" + value + '\u0001';
            if (tag10Index == -1) {
                return new Message(fixString + newTagString, new DataDictionary("/opt/sosuv/repositories/sosuv-fix-server/src/main/resources/fix/FIX44.xml"), false);
            }
            return new Message(fixString.substring(0, tag10Index) + newTagString + fixString.substring(tag10Index), new DataDictionary("/opt/sosuv/repositories/sosuv-fix-server/src/main/resources/fix/FIX44.xml"), false);
        }
    }


    public boolean evaluateIndividualCondition(Message fixMessage, String operator, int operand, Object value) throws FieldNotFound {
        boolean conditionResult = false;
        String tagValue = getTagValue(fixMessage, operand);
        if(tagValue == null)
            return conditionResult;
        switch (operator.toUpperCase()) {
            case "CONTAINS":
                conditionResult = tagValue.toUpperCase().contains(((String) value).toUpperCase());
                break;
            case "STARTS_WITH":
                conditionResult = tagValue.toUpperCase().startsWith(((String) value).toUpperCase());
                break;
            case "ENDS_WITH":
                conditionResult = tagValue.toUpperCase().endsWith(((String) value).toUpperCase());
                break;
            case "EQUALS":
                conditionResult = tagValue.toUpperCase().equals(((String) value).toUpperCase());
                break;
            case "NOT_EQUALS":
                conditionResult = !tagValue.toUpperCase().equals(((String) value).toUpperCase());
                break;
            case "IN":
                conditionResult = ((List<String>) value).contains(tagValue);
                break;
            case "NOT_IN":
                conditionResult = !((List<String>) value).contains(tagValue);
                break;
            case "NOT_NULL":
                conditionResult = tagValue != null && !tagValue.isEmpty();
                break;
            default:
                System.err.println("Invalid operator specified: " + operator);
                break;
        }
        return conditionResult;
    }

    public Message applyNormalizationRules(List<Map<String, Object>> rules, Message fixMessage) throws FieldNotFound, IncorrectTagValue, InvalidMessage, ConfigError, IOException {
        this.rules = rules;
        for (Map<String, Object> rule : rules) {
            boolean conditionsMet = true;
            List<Rule.Condition> conditions = (List<Rule.Condition>) rule.get("conditions");

            for (Rule.Condition condition : conditions) {
                String operator = (String) condition.getOperator();
                int operand = Integer.parseInt(condition.getOperand());
                Object value = condition.getValue();

                if (!evaluateIndividualCondition(fixMessage, operator, operand, value)) {
                    conditionsMet = false;
                    break;
                }
            }

            if (conditionsMet) {
                String action = (String) rule.get("action");
                Map<String, String> source = (Map<String, String>) rule.get("source");
                int target;

                switch (action.toUpperCase()) {
                    case "COPY":
                        target = Integer.parseInt((String) rule.get("target"));
                        if (source.containsKey("fix-field")) {
                            int sourceTag = Integer.parseInt(source.get("fix-field"));
                            fixMessage = setTagValue(fixMessage, target, getTagValue(fixMessage, sourceTag));
                        } else if (source.containsKey("value")) {
                            fixMessage = setTagValue(fixMessage, target, source.get("value"));
                        }
                        break;
                    case "APPEND":
                        target = Integer.parseInt((String) rule.get("target"));
                        if (source.containsKey("fix-field")) {
                            int sourceTag = Integer.parseInt(source.get("fix-field"));
                            String currentTagValue = getTagValue(fixMessage, target);
                            fixMessage = setTagValue(fixMessage, target, currentTagValue + getTagValue(fixMessage, sourceTag));
                        } else if (source.containsKey("value")) {
                            String currentTagValue = getTagValue(fixMessage, target);
                            fixMessage = setTagValue(fixMessage, target, currentTagValue + source.get("value"));
                        }
                        break;
                    case "DELETE":
                        int deleteTag = Integer.parseInt(source.get("fix-field"));
                        fixMessage.removeField(deleteTag);
                        break;
                    case "MOVE":
                        target = Integer.parseInt((String) rule.get("target"));
                        int sourceTagMove = Integer.parseInt(source.get("fix-field"));
                        fixMessage = setTagValue(fixMessage, target, getTagValue(fixMessage, sourceTagMove));
                        fixMessage.removeField(sourceTagMove);
                        break;
                    case "SET":
                        target = Integer.parseInt((String) rule.get("target"));
                        String setValue = conditions.get(0).getOperand().toString();
                        fixMessage = setTagValue(fixMessage, target, setValue);
                        break;
                    default:
                        System.err.println("Invalid action specified: " + action);
                        break;
                }
            }
        }
        return fixMessage;
    }
}
