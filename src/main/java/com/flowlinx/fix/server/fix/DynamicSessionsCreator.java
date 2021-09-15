package com.flowlinx.fix.server.fix;

import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import com.flowlinx.fix.server.service.DynamicSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quickfix.ThreadedSocketAcceptor;

import java.util.List;

@Component
public class DynamicSessionsCreator {

    @Autowired
    private DynamicSessionService dynamicSessionService;

    @Scheduled(initialDelay = 5000, fixedDelay = Integer.MAX_VALUE)
    public void initDynamicSession() {
        final List<CreateSessionRepresentation> sessions = dynamicSessionService.getAllDynamicSessions();
        dynamicSessionService.addDynamicSessions( sessions, false );
    }
}
