package com.flowlinx.fix.server.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixMessageRepo extends PagingAndSortingRepository<FixMessage, Long> {
}
