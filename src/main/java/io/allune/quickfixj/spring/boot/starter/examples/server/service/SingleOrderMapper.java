package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure.OrderRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import quickfix.FieldNotFound;
import quickfix.fix44.NewOrderSingle;

@Mapper(componentModel = "spring")
public interface SingleOrderMapper {

    @Mappings({ @Mapping(source = "price.value", target = "price"),
            @Mapping(source = "orderQty.value", target = "quantity"),
            @Mapping(source = "symbol.value", target = "ticker"),
            @Mapping(source = "securityID.value", target = "exchangeCode"),
            @Mapping(source = "transactTime.value", target = "operationDate"),
//            @Mapping(target = "brokerName", expression = "java(item.getField(new quickfix.field.SenderCompID()).getValue())"),
            @Mapping(source = "side.value", target = "operation")})
    OrderRepresentation messageToOrder(NewOrderSingle item) throws FieldNotFound;

}
