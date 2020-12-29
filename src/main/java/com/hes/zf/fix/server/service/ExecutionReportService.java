package com.hes.zf.fix.server.service;

import lombok.extern.log4j.Log4j2;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

import java.time.LocalDateTime;
import java.util.Random;

@Log4j2
public class ExecutionReportService {

    public static void send(NewOrderSingle order, OrdStatus ordStatus) {
        try {
            Account account = new Account(order.getAccount().getValue());

            AvgPx avgPx = new AvgPx(0);
            ClOrdID clOrdID = new ClOrdID("ID:" + order.getAccount().getValue());
            CumQty cumQty = new CumQty(0);
            Currency currency = new Currency(order.getCurrency().getValue());
            ExecID execID = new ExecID(String.valueOf(new Random().nextInt()));
            IDSource idSource = new IDSource(order.getField(new IDSource()).getValue());
            LastCapacity lastCapacity = new LastCapacity('1');
            LastMkt lastMkt = new LastMkt("lastmkt");
            LastQty lastQty = new LastQty(0);
            LastPx lastPx = new LastPx(0);
            OrderID orderID = new OrderID("NONE");
            OrderQty orderQty = new OrderQty(order.getOrderQty().getValue());
            OrdType ordType = new OrdType(order.getOrdType().getValue());
            OrigClOrdID origClOrdID = new OrigClOrdID(order.getClOrdID().getValue());
            Price price = new Price(order.getPrice().getValue());
            SecurityID securityID = new SecurityID(order.getSecurityID().getValue());
            Side side = new Side(order.getSide().getValue());
            Symbol symbol = new Symbol(order.getSymbol().getValue());
            Text text = new Text("Free format");
            TimeInForce time = new TimeInForce('0');
            TransactTime transactTime = new TransactTime(LocalDateTime.now());
            MinQty minQty = new MinQty(order.getMinQty().getValue());
            ExpireTime expireTime = new ExpireTime(order.getExpireTime().getValue());
            ExecType execType = new ExecType('8');
            LeavesQty leavesQty = new LeavesQty(order.getOrderQty().getValue());
            SecurityExchange securityExchange = new SecurityExchange("FIX 4.4 ISO MIC");

            ExecutionReport message = new ExecutionReport();
            message.setField(account);
            message.setField(avgPx);
            message.setField(clOrdID);
            message.setField(cumQty);
            message.setField(currency);
            message.setField(execID);
            message.setField(idSource);
            message.setField(lastCapacity);
            message.setField(lastMkt);
            message.setField(lastQty);
            message.setField(lastPx);
            message.setField(orderID);
            message.setField(orderQty);
            message.setField(ordType);
            message.setField(origClOrdID);
            message.setField(price);
            message.setField(securityID);
            message.setField(side);
            message.setField(symbol);
            message.setField(text);
            message.setField(time);
            message.setField(transactTime);
            message.setField(minQty);
            message.setField(expireTime);
            message.setField(execType);
            message.setField(leavesQty);
            message.setField(securityExchange);
            message.setField(ordStatus);

            Session.sendToTarget(message, "SERVER", "CLIENT");

        } catch (FieldNotFound fieldNotFound) {
            log.error("FIELD NOT FOUND field={}", fieldNotFound.field);
            fieldNotFound.printStackTrace();
        } catch (SessionNotFound sessionNotFound) {
            log.error("Session not found", sessionNotFound.getMessage());
            sessionNotFound.printStackTrace();

        }


    }

}
