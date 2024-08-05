package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.RoutingTable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientordidBuysideMappingRepository extends AbstractRepository<ClientordidBuysideMapping> {

    Optional<ClientordidBuysideMapping> findByClientOrderId(String deliverToCompID);
}
