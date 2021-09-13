package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.FixMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<FixMessage, FixMessage.PK> {

    Page<FixMessage> findByTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
