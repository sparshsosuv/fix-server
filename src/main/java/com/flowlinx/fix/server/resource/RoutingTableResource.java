package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.representation.PageRepresentation;
import com.flowlinx.fix.server.service.RoutingTableService;
import com.flowlinx.fix.server.utils.AppConstants;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Secured(AppConstants.ROLE_ADMIN)
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/routing/table")
public class RoutingTableResource {

    @Autowired
    private RoutingTableService service;

    @PostMapping
    public ResponseEntity<RoutingTable> create(RoutingTable routingTable){
        return ResponseEntity.ok( service.save( routingTable ) );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(RoutingTable routingTable){
        service.delete( routingTable );
        return ResponseEntity.ok().build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = {"/page/{page}/size/{size}/sort/{sort}/desc/{desc}"})
    public ResponseEntity<PageRepresentation<RoutingTable>> filter(
            @PathVariable(value = "page") Integer page,
            @PathVariable(value = "size") Integer size,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "desc") boolean desc,
            RoutingTable filter) {

        final Pageable pageable = getPageable(page, size, sort, desc);
        final Page<RoutingTable> result = service.findAll( pageable, new EntityFilter<>( filter ) );

        return ResponseEntity.ok( new PageRepresentation( result, result.getContent(), desc ) );
    }

    public static Pageable getPageable(final Integer page, final Integer size, final String sort, final boolean desc) {

        final Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(25),
                direction,
                Optional.ofNullable(sort).orElse("deliverToCompID"));
    }

}