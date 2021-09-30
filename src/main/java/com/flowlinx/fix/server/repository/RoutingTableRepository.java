package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.RoutingTable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoutingTableRepository extends AbstractRepository<RoutingTable> {

    Optional<RoutingTable> findByDeliverToCompID(String deliverToCompID);
}
