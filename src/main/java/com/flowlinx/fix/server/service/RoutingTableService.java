package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.exception.PreconditionalFailedException;
import com.flowlinx.fix.server.repository.RoutingTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoutingTableService extends AbstractBaseService<RoutingTable>{

	private final RoutingTableRepository repo;

	@Autowired
	public RoutingTableService(RoutingTableRepository repo) {
		super(repo);
		this.repo = repo;
	}

	public Optional<RoutingTable> findByDeliverToCompID( String deliverToCompId ){
		return repo.findByDeliverToCompID( deliverToCompId );
	}

   	@Override
	public Specification<RoutingTable> specificationByFilter(EntityFilter<RoutingTable> filter) {
		Specification<RoutingTable> spec = notNull("id");

		if( filter.getCondition() != null && filter.getCondition().getDeliverToCompID() != null ) {
			final String deliverToCompID = filter.getCondition().getDeliverToCompID();
			spec = ( deliverToCompID != null ) ? ilike( "deliverToCompID", deliverToCompID ) : spec;
		}

      	return spec;
	}

	@Transactional
	public RoutingTable save(RoutingTable item) {

		if( findByDeliverToCompID( item.getDeliverToCompID() ).isPresent() ) {
			throw new PreconditionalFailedException("DeliverToCompID " + item.getDeliverToCompID() + " already exists.");
		}

		return super.save( item );
	}

	@Transactional
	public RoutingTable update(Long id, RoutingTable item) {
		final RoutingTable routingTable = findById( id ).get();

		if( !routingTable.getDeliverToCompID().equalsIgnoreCase( item.getDeliverToCompID() )
				&& findByDeliverToCompID( item.getDeliverToCompID() ).isPresent() ) {
			throw new PreconditionalFailedException("DeliverToCompID " + item.getDeliverToCompID() + " already exists.");
		}

		routingTable.setDeliverToCompID( item.getDeliverToCompID() );
		routingTable.setSenderCompID( item.getSenderCompID() );
		routingTable.setTargetCompID( item.getTargetCompID() );

		return super.save( routingTable );
	}

	@Transactional
	public void delete(Long id) {
		final RoutingTable routingTable = findById( id ).get();
		delete( routingTable );
	}
}
