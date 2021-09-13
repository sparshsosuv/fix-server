package com.flowlinx.fix.server.domain;

import java.io.Serializable;

public interface FixEntity extends Serializable {

    public String getText();

    public void setMsgType(String type);
}
