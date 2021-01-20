package com.flowlinx.fix.server.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FixMessageRepo extends PagingAndSortingRepository<FixMessage, Long> {

    Page<FixMessage> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
