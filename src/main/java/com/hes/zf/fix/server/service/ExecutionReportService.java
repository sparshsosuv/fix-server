package com.hes.zf.fix.server.service;

import lombok.extern.log4j.Log4j2;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

import java.time.LocalDateTime;

@Log4j2
public class ExecutionReportService {

    public static void send(NewOrderSingle order, OrdStatus ordStatus) {

        log.info("m=send , sending order:{}", order);
        ExecutionReport message = new ExecutionReport();

        //FIXME refactoring
        ClOrdID clOrdID = null; // new ClOrdID(); //11
        try {
            clOrdID = order.getClOrdID();
            message.setField(clOrdID);
        } catch (FieldNotFound fieldNotFound) {
            log.error("ClOrdID not found", fieldNotFound);
        }
        Currency currency = null;// new Currency(); //15
        try {
            currency = order.getCurrency();
            message.setField(currency);
        } catch (FieldNotFound fieldNotFound) {
            log.error("currency not found",fieldNotFound);
        }
        OrderID orderID = null; //37
        try {
            orderID = new OrderID("reportO" + order.getAccount()); //37
            message.setField(orderID);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Account Field for orderId not found",fieldNotFound);
        }

        Price price = null; //44
        try {
            price = order.getPrice();
            message.setField(price);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Price Field not found",fieldNotFound);
        }
        ExecID execID = null;
        try{
            execID = new ExecID("report" + order.getAccount()); //17
            message.setField(execID);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Account Field for ExecID not found",fieldNotFound);
        }

        Side side = null; // new Side(); //54
        try {
            side = order.getSide();
            message.setField(side);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Side Field not found",fieldNotFound);
        }
        Symbol symbol = null; //new Symbol(); //55
        try {
            symbol = order.getSymbol();
            message.setField(symbol);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Symbol Field not found",fieldNotFound);
        }
        Text text = null; //  new Text(); //58
        try {
            text = order.getText();
            message.setField(text);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Text Field not found",fieldNotFound);
        }
        SecurityID securityID = null; //new SecurityID(); //48
        try {
            securityID = order.getSecurityID();
            message.setField(securityID);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Security Id Field not found",fieldNotFound);
        }
        TimeInForce timeInForce = null; // new TimeInForce(); //59
        try {
            timeInForce = order.getTimeInForce();
            message.setField(timeInForce);
        } catch (FieldNotFound fieldNotFound) {
            log.error("TimeInForce Field not found",fieldNotFound);
        }
        OrdType ordType = null; //new OrdType(); //40
        try {
            ordType = order.getOrdType();
            message.setField(ordType);
        } catch (FieldNotFound fieldNotFound) {
            log.error("OrdType Field not found",fieldNotFound);
        }
        Account account = null; //new Account(); //01
        try {
            account = order.getAccount();
            message.setField(account);
        } catch (FieldNotFound fieldNotFound) {
            log.error("Account Field not found",fieldNotFound);
        }
        MinQty minQty = null;
        try {
            minQty =order.getMinQty();
            message.setField(minQty);
        } catch (FieldNotFound fieldNotFound) {
            log.error("MinQty Field not found",fieldNotFound);
        }

        SenderCompID senderCompID = new SenderCompID(); // 49
        DeliverToSubID id = new DeliverToSubID(); // 129
        DeliverToCompID deliverToCompID = new DeliverToCompID(); // 128
        TargetCompID targetCompID = new TargetCompID(); // 56
        SendingTime sendingTime = new SendingTime(LocalDateTime.now());// new SendingTime(); // 52

        AvgPx avgPx = new AvgPx(0); //06
        CumQty cumQty = new CumQty(0); // 14
        IDSource idSource = new IDSource(); //22
        LastCapacity lastCapacity = new LastCapacity('1'); //29
        LastMkt lastMkt = new LastMkt(); //30 //discuss yan
        LastPx lastPx = new LastPx(0); //31
        LastShares lastShares = new LastShares(0); //32
        OrderQty orderQty = new OrderQty(0); //38
        OrigClOrdID origClOrdID = new OrigClOrdID(); //41 order.getOrigClOrdID
        TransactTime transactTime = new TransactTime(); //60
        ExecBroker execBroker = new ExecBroker(); //76
        ExpireTime expireTime = new ExpireTime(); // 126
        ExecType execType = new ExecType(); //150
        LeavesQty leavesQty = new LeavesQty(); //151
        SecurityExchange securityExchange = new SecurityExchange(); //207


        message.setField(senderCompID);
        message.setField(id);
        message.setField(deliverToCompID);
        message.setField(targetCompID);
        message.setField(ordStatus);
        message.setField(sendingTime);
        message.setField(avgPx);
        message.setField(cumQty);
        message.setField(idSource);
        message.setField(lastCapacity);
        message.setField(lastMkt);
        message.setField(lastPx);
        message.setField(lastShares);
        message.setField(orderQty);
        message.setField(origClOrdID);
        message.setField(transactTime);
        message.setField(execBroker);
        message.setField(expireTime);
        message.setField(execType);
        message.setField(leavesQty);
        message.setField(securityExchange);


        try {
            Session.sendToTarget(message, "SERVER", "CLIENT");
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
        }

    }
}
