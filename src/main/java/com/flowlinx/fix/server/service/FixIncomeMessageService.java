package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.FixIncomeMessage;
import com.flowlinx.fix.server.repository.FixIncomeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FixIncomeMessageService extends AbstractBaseService<FixIncomeMessage>{

	@Autowired
	public FixIncomeMessageService(FixIncomeMessageRepository repository) {
		super(repository);
	}

   	@Override
	public Specification<FixIncomeMessage> specificationByFilter(EntityFilter<FixIncomeMessage> filter) {
		Specification<FixIncomeMessage> spec = notNull();

		if( filter.getCondition() != null ) {
			final String sendercompid = filter.getCondition().getSendercompid();
			final String targetcompid = filter.getCondition().getTargetcompid();

			spec = ( sendercompid != null ) ? ilike( "sendercompid", sendercompid ) : spec;
			spec = ( targetcompid != null ) ? spec.or( ilike( "targetcompid", targetcompid ) ) : spec;
		}

      	return spec;
	}

}
