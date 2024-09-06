package com.flowlinx.fix.server.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnClientEnabledCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        return env.getProperty("fix.mode", "both").equalsIgnoreCase("client")
                || env.getProperty("fix.mode", "both").equalsIgnoreCase("both");
    }
}
