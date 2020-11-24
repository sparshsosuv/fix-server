package io.allune.quickfixj.spring.boot.starter.examples.server.infrastructure;

import java.io.Serializable;
import java.math.BigDecimal;


public class OrderRepresentation implements Serializable {

    private static final long serialVersionUID = 8124875229029382991L;

    private String orderId;

    private String brokerName;

    private BigDecimal price;

    private Integer quantity;

    private Operation operation;

    private String ticker;

    private String exchangeCode;

    private String operationDate;

    private String mainType;

    private String fileName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", brokerName=" + brokerName + ", price=" + price
                + ", quantity=" + quantity + ", operation=" + operation + ", ticker=" + ticker + ", exchangeCode="
                + exchangeCode + ", operationDate=" + operationDate + ", mainType=" + mainType + ", fileName="
                + fileName + "]";
    }

}
