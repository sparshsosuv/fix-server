package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.FixSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FixSessionRepository extends PagingAndSortingRepository<FixSession, FixSession.PK> {

    Page<FixSession> findByTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
