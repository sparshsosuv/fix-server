package io.allune.quickfixj.spring.boot.starter.examples.server.service;

import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

public class ExecutionReportService {

    public static void send(ExecutionReportRepresentation report, NewOrderSingle order) throws FieldNotFound {

        ExecutionReport message = new ExecutionReport();


        order.getAccount();
        order.getClOrdID();


        SenderCompID senderCompID = new SenderCompID(); // 49
        DeliverToSubID id = new DeliverToSubID(); // 129
        DeliverToCompID deliverToCompID = new DeliverToCompID(); // 128
        TargetCompID targetCompID = new TargetCompID(); // 56
        SendingTime sendingTime = new SendingTime(); // 52
        Account account = new Account(); //01
        AvgPx avgPx = new AvgPx(); //06
        ClOrdID clOrdID = new ClOrdID(); //11
        CumQty cumQty = new CumQty(); // 14
        Currency currency = new Currency(); //15
        ExecID execID = new ExecID(); //17
        IDSource idSource = new IDSource(); //22
        LastCapacity lastCapacity = new LastCapacity(); //29
        LastMkt lastMkt = new LastMkt(); //30
        LastPx lastPx = new LastPx(); //31
        LastShares lastShares = new LastShares(); //32
        OrderID orderID = new OrderID(); //37
        OrderQty orderQty = new OrderQty(); //38
        OrdStatus ordStatus = new OrdStatus(); //39
        OrdType ordType = new OrdType(); //40
        OrigClOrdID origClOrdID = new OrigClOrdID(); //41
        Price price = new Price(); //44
        SecurityID securityID = new SecurityID(); //48
        Side side = new Side(); //54
        Symbol symbol = new Symbol(); //55
        Text text = new Text(); //58
        TimeInForce timeInForce = new TimeInForce(); //59
        TransactTime transactTime = new TransactTime(); //60
        ExecBroker execBroker = new ExecBroker(); //76
        MinQty minQty = new MinQty(); // 110
        ExpireTime expireTime = new ExpireTime(); // 126
        ExecType execType = new ExecType(); //150
        LeavesQty leavesQty = new LeavesQty(); //151
        SecurityExchange securityExchange = new SecurityExchange(); //207












        ordStatus.setValue('2');
        orderID.setValue("id");
        order.getSymbol();

        message.setField(ordStatus);
        message.setField(orderID);

        //55
        //ioi.setString( 8002 , "CONDITIONAL ORDER");

        try {
            Session.sendToTarget(message, "SERVER", "CLIENT");
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
        }

    }
}
