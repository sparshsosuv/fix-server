package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository extends MongoRepository<Rule, String> {
}
