package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.activity.DynamicSessionActivity;
import com.flowlinx.fix.server.resource.representation.CreateSessionRepresentation;
import com.flowlinx.fix.server.type.Authority;
import com.flowlinx.fix.server.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Secured(AppConstants.ROLE_ADMIN)
@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/fix/dynamic/session")
public class DynamicSessionResource {

    @Autowired
    private DynamicSessionActivity activity;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateSessionRepresentation item) {
        activity.create( item );
        return ResponseEntity.ok().build();
    }
}
