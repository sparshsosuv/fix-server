package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.FixSessionExt;
import com.flowlinx.fix.server.domain.QueuedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixSessionExtRepository extends JpaRepository<FixSessionExt, String> {
    FixSessionExt findBySessionId(String sessionId);
    FixSessionExt findBySessionIdAndFixVersion(String sessionId, String fixVersion);
}
