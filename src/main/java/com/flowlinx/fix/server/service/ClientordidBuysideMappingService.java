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

    public Optional<ClientordidBuysideMapping> findByMsgTypeAndMsgSeqNumAndSenderAndTargetAndFixVersion( String msgType, Long msgSeqNum, String sender, String target, String fixVersion ){
        System.out.println("findByMsgTypeAndMsgSeqNumAndSenderAndTargetAndFixVersion......");
        return repo.findByMsgTypeAndMsgSeqNumAndSenderAndTargetAndFixVersion( msgType, msgSeqNum, sender, target, fixVersion );
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
            existingItem.get().setMessage(item.getMessage());
            existingItem.get().setMsgType(item.getMsgType());
            existingItem.get().setMsgSeqNum(item.getMsgSeqNum());
            existingItem.get().setSender(item.getSender());
            existingItem.get().setTarget(item.getTarget());
            existingItem.get().setFixVersion(item.getFixVersion());
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
        clientordidBuysideMapping.setMessage( item.getMessage() );
        clientordidBuysideMapping.setMsgType(item.getMsgType());
        clientordidBuysideMapping.setMsgSeqNum(item.getMsgSeqNum());
        clientordidBuysideMapping.setSender(item.getSender());
        clientordidBuysideMapping.setTarget(item.getTarget());
        clientordidBuysideMapping.setFixVersion(item.getFixVersion());

        return super.save( clientordidBuysideMapping );
    }

    @Transactional
    public void delete(Long id) {
        final ClientordidBuysideMapping clientordidBuysideMapping = findById( id ).get();
        delete( clientordidBuysideMapping );
    }
}
