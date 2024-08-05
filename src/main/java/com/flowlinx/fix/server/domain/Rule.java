package com.flowlinx.fix.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "rules")
public class Rule {
    @Id
    private String id;
    private String action;
    private Map<String, String> source;
    private String target;
    private String value;
    private List<Condition> conditions;

    // Getters and setters

    public static class Condition {
        private String operand;
        private String operator;
        private Object value;  // Object to handle both String and List<String>

        public String getOperand() {
            return operand;
        }

        public void setOperand(String operand) {
            this.operand = operand;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        // Getters and setters
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getSource() {
        return source;
    }

    public void setSource(Map<String, String> source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
// Getters and setters for Rule class
}
