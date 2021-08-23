package com.flowlinx.fix.server;

import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import com.flowlinx.fix.server.service.DynamicSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.ThreadedSocketAcceptor;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DynamicSessionsCreator {

    @Autowired
    private DynamicSessionService dynamicSessionService;

    @Autowired
    private ThreadedSocketAcceptor socketAcceptor;

    @PostConstruct
    public void initDynamicSession() {
        List<CreateSessionRepresentation> allDynamicSessions = dynamicSessionService.getAllDynamicSessions();

        dynamicSessionService.addDynamicSessions( allDynamicSessions, false );
    }
}
