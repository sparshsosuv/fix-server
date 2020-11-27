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
            @Mapping(source = "clOrdID.value", target = "orderId"),


//            @Mapping(target = "brokerName", expression = "java(item.getField(new quickfix.field.SenderCompID()).getValue())"),
            @Mapping(target = "operation", expression = "java(Character.getNumericValue(item.getSide().getValue()))")})
    OrderRepresentation messageToOrder(NewOrderSingle item) throws FieldNotFound;

//    private String orderId; ** clOrdID
//    private String brokerName; ** sendercomp
//    private BigDecimal price; **
//    private Integer quantity; **
//    private Operation operation; ** Side
//    private String ticker; **
//    private String exchangeCode; **
//    private String operationDate; **
//    private String mainType;
//    private String fileName;


//    IDSource idSource = new IDSource();
//    SenderCompID senderCompID = new SenderCompID();
//    ClOrdID clOrdID = new ClOrdID();
//    HandlInst handlInst = new HandlInst();
//    Symbol symbol = new Symbol(); ** ticker
//    Side side = new Side(); ** operation
//    TransactTime transactTime = new TransactTime();
//    OrdType ordType = new OrdType();
//    Currency currency = new Currency();
//    OrderQty orderQty = new OrderQty(); **
//    Price price = new Price();   **
//    SecurityID securityID = new SecurityID(); ** exchangeCode
//    Text text = new Text();
//    TimeInForce timeInForce = new TimeInForce();
//    TargetCompID targetCompID = new TargetCompID();

}
