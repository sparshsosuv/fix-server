package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.FixOutgoingMessage;
import com.flowlinx.fix.server.repository.FixOutgoingMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FixOutgoingMessageService extends AbstractBaseService<FixOutgoingMessage>{

	@Autowired
	public FixOutgoingMessageService(FixOutgoingMessageRepository repository) {
		super(repository);
	}

   	@Override
	public Specification<FixOutgoingMessage> specificationByFilter(EntityFilter<FixOutgoingMessage> filter) {
		Specification<FixOutgoingMessage> spec = notNull();

		if( filter.getCondition() != null ) {
			final String sendercompid = filter.getCondition().getSendercompid();
			final String targetcompid = filter.getCondition().getTargetcompid();

			spec = ( sendercompid != null ) ? ilike( "sendercompid", sendercompid ) : spec;
			spec = ( targetcompid != null ) ? spec.or( ilike( "targetcompid", targetcompid ) ) : spec;
		}

      	return spec;
	}

}
