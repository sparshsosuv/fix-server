package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

import java.time.LocalDateTime;

public class ExecutionReportService {

    public static void send(ExecutionReportRepresentation report, NewOrderSingle order) throws FieldNotFound {

        ExecutionReport message = new ExecutionReport();


        order.getAccount();
        order.getClOrdID();

        SenderCompID senderCompID = new SenderCompID(); // 49
        DeliverToSubID id = new DeliverToSubID(); // 129
        DeliverToCompID deliverToCompID = new DeliverToCompID(); // 128
        TargetCompID targetCompID = new TargetCompID(); // 56
        SendingTime sendingTime = new SendingTime(LocalDateTime.now());// new SendingTime(); // 52
        Account account = order.getAccount(); //new Account(); //01
        AvgPx avgPx = new AvgPx(0); //06
        ClOrdID clOrdID = order.getClOrdID(); // new ClOrdID(); //11
        CumQty cumQty = new CumQty(); // 14
        Currency currency = order.getCurrency();// new Currency(); //15
        ExecID execID = new ExecID(); //17
        IDSource idSource = new IDSource(); //22
        LastCapacity lastCapacity = new LastCapacity('1'); //29
        LastMkt lastMkt = new LastMkt(); //30
        LastPx lastPx = new LastPx(0); //31
        LastShares lastShares = new LastShares(0); //32
        OrderID orderID = new OrderID(); //37
        OrderQty orderQty = new OrderQty(); //38
        OrdStatus ordStatus = new OrdStatus(); //39
        OrdType ordType = order.getOrdType(); //new OrdType(); //40
        OrigClOrdID origClOrdID = new OrigClOrdID(); //41 order.getOrigClOrdID
        Price price = order.getPrice(); //44
        SecurityID securityID = order.getSecurityID(); //new SecurityID(); //48
        Side side = order.getSide(); // new Side(); //54
        Symbol symbol = order.getSymbol(); //new Symbol(); //55
        Text text = order.getText(); //  new Text(); //58
        TimeInForce timeInForce = order.getTimeInForce(); // new TimeInForce(); //59
        TransactTime transactTime = new TransactTime(); //60
        ExecBroker execBroker = new ExecBroker(); //76
        MinQty minQty = new MinQty(); // 110
        ExpireTime expireTime = new ExpireTime(); // 126
        ExecType execType = new ExecType(); //150
        LeavesQty leavesQty = new LeavesQty(); //151
        SecurityExchange securityExchange = new SecurityExchange(); //207


        message.set(order.getMinQty());


        message.setField(ordStatus);
        message.setField(orderID);
        message.setField(price);
        message.setField(account);
        message.setField(sendingTime);
        message.setField(avgPx);
        message.setField(clOrdID);
        message.setField(cumQty);
        message.setField(currency);
        message.setField(execID);
        message.setField(idSource);
        message.setField(lastCapacity);
        message.setField(lastMkt);
        message.setField(lastPx);
        message.setField(lastShares);
        message.setField(orderQty);
        message.setField(ordType);
        message.setField(origClOrdID);
        message.setField(securityID);
        message.setField(side);
        message.setField(symbol);
        message.setField(text);
        message.setField(timeInForce);
        message.setField(transactTime);
        message.setField(execBroker);
        message.setField(minQty);
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
