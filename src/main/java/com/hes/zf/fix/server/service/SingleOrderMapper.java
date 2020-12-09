package com.hes.zf.fix.server.service;

import com.hes.zf.fix.server.infrastructure.OrderRepresentation;
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
            @Mapping(source = "securityExchange.value", target = "exchangeCode"),
            @Mapping(source = "transactTime.value", target = "operationDate"),
            @Mapping(source = "clOrdID.value", target = "orderId"),
            @Mapping(target = "brokerName", expression = "java(item.getHeader().getString(quickfix.field.SenderCompID.FIELD))"),
            @Mapping(target = "operation", expression = "java(Character.getNumericValue(item.getSide().getValue()))")})
    OrderRepresentation messageToOrder(NewOrderSingle item) throws FieldNotFound;

}
