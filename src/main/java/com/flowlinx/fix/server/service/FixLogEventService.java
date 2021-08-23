package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.FixLogEvent;
import com.flowlinx.fix.server.repository.FixLogEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FixLogEventService extends AbstractBaseService<FixLogEvent>{

	@Autowired
	public FixLogEventService(FixLogEventRepository repository) {
		super(repository);
	}

   	@Override
	public Specification<FixLogEvent> specificationByFilter(EntityFilter<FixLogEvent> filter) {
		Specification<FixLogEvent> spec = notNull();

		if( filter.getCondition() != null ) {
			final String sendercompid = filter.getCondition().getSendercompid();
			final String targetcompid = filter.getCondition().getTargetcompid();

			spec = ( sendercompid != null ) ? ilike( "sendercompid", sendercompid ) : spec;
			spec = ( targetcompid != null ) ? spec.or( ilike( "targetcompid", targetcompid ) ) : spec;
		}

      	return spec;
	}

}
