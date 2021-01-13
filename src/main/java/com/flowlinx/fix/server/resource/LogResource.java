package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.persistence.FixMessage;
import com.flowlinx.fix.server.persistence.FixMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/logs")
public class LogResource {

    @Autowired
    private FixMessageRepo fixMessageRepo;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Iterable<FixMessage>> logs() {
        return ResponseEntity.ok().body( fixMessageRepo.findAll( Sort.by("id").descending() ) );
    }

}