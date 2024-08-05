package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.QueuedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueuedMessageRepository extends JpaRepository<QueuedMessage, Long> {
    List<QueuedMessage> findByProcessedFalse();
}
