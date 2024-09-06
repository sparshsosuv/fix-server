package com.flowlinx.fix.server.activity;

import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import com.flowlinx.fix.server.resource.representation.DeleteSessionRepresentation;
import com.flowlinx.fix.server.service.DynamicSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DynamicSessionActivity {

    @Autowired
    private DynamicSessionService service;

    public void create( CreateSessionRepresentation sessionRepresentation ) {
        service.create( sessionRepresentation );
    }

    public void update( CreateSessionRepresentation sessionRepresentation ) {
        service.update( sessionRepresentation );
    }

    public void delete( DeleteSessionRepresentation sessionRepresentation ) {
        service.delete( sessionRepresentation );
    }

}
