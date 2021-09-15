package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.DynamicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicSessionRepository extends JpaRepository<DynamicSession, Long> {

}
