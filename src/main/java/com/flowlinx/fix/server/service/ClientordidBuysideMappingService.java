package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.ClientordidBuysideMapping;
import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.domain.RoutingTable;
import com.flowlinx.fix.server.exception.PreconditionalFailedException;
import com.flowlinx.fix.server.repository.ClientordidBuysideMappingRepository;
import com.flowlinx.fix.server.repository.RoutingTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientordidBuysideMappingService extends AbstractBaseService<ClientordidBuysideMapping>{

    private final ClientordidBuysideMappingRepository repo;

    @Autowired
    public ClientordidBuysideMappingService(ClientordidBuysideMappingRepository repo) {
        super(repo);
        this.repo = repo;
    }

    public Optional<ClientordidBuysideMapping> findByClientOrderId( String clientOrderId ){
        return repo.findByClientOrderId( clientOrderId );
    }

//    @Override
//    public Specification<RoutingTable> specificationByFilter(EntityFilter<RoutingTable> filter) {
//        Specification<RoutingTable> spec = notNull("id");
//
//        if( filter.getCondition() != null && filter.getCondition().getDeliverToCompID() != null ) {
//            final String deliverToCompID = filter.getCondition().getDeliverToCompID();
//            spec = ( deliverToCompID != null ) ? ilike( "deliverToCompID", deliverToCompID ) : spec;
//        }
//
//        return spec;
//    }

//    @Transactional
//    public ClientordidBuysideMapping save(ClientordidBuysideMapping item) {
//
////        Optional<ClientordidBuysideMapping> clientordidBuysideMapping = findByClientOrderId( item.getClientOrderId() );
////        if( clientordidBuysideMapping.isPresent() ) {
////            Long id = clientordidBuysideMapping.get().getId();
////            update(id, item);
//////            throw new PreconditionalFailedException("clientOrderId " + item.getClientOrderId() + " already exists.");
////        }
//
//        return super.save( item );
//    }


    @Transactional
    public ClientordidBuysideMapping save(ClientordidBuysideMapping item) {
        Optional<ClientordidBuysideMapping> existingItem = findByClientOrderId(item.getClientOrderId());
        if (existingItem.isPresent()) {
            existingItem.get().setBuySideSession(item.getBuySideSession());
            return super.save(existingItem.get());
        } else {
            return super.save(item);
        }
    }

    @Transactional
    public ClientordidBuysideMapping update(Long id, ClientordidBuysideMapping item) {
        final ClientordidBuysideMapping clientordidBuysideMapping = findById( id ).get();

        if( !clientordidBuysideMapping.getClientOrderId().equalsIgnoreCase( item.getClientOrderId() )
                && findByClientOrderId( item.getClientOrderId() ).isPresent() ) {
            throw new PreconditionalFailedException("clientOrderId " + item.getClientOrderId() + " already exists.");
        }

        clientordidBuysideMapping.setClientOrderId( item.getClientOrderId() );
        clientordidBuysideMapping.setBuySideSession( item.getBuySideSession() );

        return super.save( clientordidBuysideMapping );
    }

    @Transactional
    public void delete(Long id) {
        final ClientordidBuysideMapping clientordidBuysideMapping = findById( id ).get();
        delete( clientordidBuysideMapping );
    }
}
