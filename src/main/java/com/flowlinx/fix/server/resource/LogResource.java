package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.persistence.FixMessage;
import com.flowlinx.fix.server.persistence.FixMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/logs")
public class LogResource {

    @Autowired
    private FixMessageRepo fixMessageRepo;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = {"/sort/{sort}/desc/{desc}"})
    public ResponseEntity<Page<FixMessage>> filter(
            @PathVariable(value = "sort", required = false) String sort,
            @PathVariable(value = "desc", required = false) boolean desc,
            @RequestParam("pageSize") Integer size,
            @RequestParam("pageNumber") Integer page,
            String filter) {

        final LocalDateTime start = LocalDateTime.now().minusHours(24);
        final LocalDateTime finalDate = LocalDateTime.now();

        final Pageable pageable = getPageable(page - 1, size, sort, desc);

        final Page<FixMessage> messages = fixMessageRepo.findByCreatedAtBetween(start, finalDate, pageable);

        return ResponseEntity.ok(messages);
    }

    public static Pageable getPageable(final Integer page, final Integer size, final String sort, final boolean desc) {

        final Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(25),
                direction,
                Optional.ofNullable(sort).orElse("id"));
    }

}