package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

import java.time.LocalDateTime;

public class ExecutionReportService {

    public static void send(ExecutionReportRepresentation report, NewOrderSingle order)  {

        ExecutionReport message = new ExecutionReport();

        try {
            ClOrdID clOrdID = order.getClOrdID(); // new ClOrdID(); //11
            Currency currency = order.getCurrency();// new Currency(); //15
            OrderID orderID = new OrderID("reportO" + order.getAccount()); //37
            Price price = order.getPrice(); //44
            ExecID execID = new ExecID("report" + order.getAccount()); //17
            Side side = order.getSide(); // new Side(); //54
            Symbol symbol = order.getSymbol(); //new Symbol(); //55
            Text text = order.getText(); //  new Text(); //58
            SecurityID securityID = order.getSecurityID(); //new SecurityID(); //48
            TimeInForce timeInForce = order.getTimeInForce(); // new TimeInForce(); //59
            OrdType ordType = order.getOrdType(); //new OrdType(); //40
            Account account = order.getAccount(); //new Account(); //01

            message.setField(orderID);
            message.setField(price);
            message.setField(account);
            message.setField(clOrdID);
            message.setField(currency);
            message.setField(execID);
            message.setField(ordType);
            message.setField(securityID);
            message.setField(side);
            message.setField(symbol);
            message.setField(text);
            message.setField(timeInForce);
            message.set(order.getMinQty());

        } catch (FieldNotFound ex) {
            System.out.println("field not found exception");
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
        OrdStatus ordStatus = new OrdStatus('8'); //39
        OrigClOrdID origClOrdID = new OrigClOrdID(); //41 order.getOrigClOrdID
        TransactTime transactTime = new TransactTime(); //60
        ExecBroker execBroker = new ExecBroker(); //76
        MinQty minQty = new MinQty(); // 110
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
