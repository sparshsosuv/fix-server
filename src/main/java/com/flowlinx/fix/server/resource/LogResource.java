package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.persistence.FixMessage;
import com.flowlinx.fix.server.persistence.FixMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/logs")
public class LogResource {

    @Autowired
    private FixMessageRepo fixMessageRepo;

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Iterable<FixMessage>> logs() {

        final PageRequest pageable = PageRequest.of( 0, 100, Sort.by("id").descending() );

        final Iterable<FixMessage> messages = fixMessageRepo.findAll( pageable );

        return ResponseEntity.ok().body( messages );
    }

}