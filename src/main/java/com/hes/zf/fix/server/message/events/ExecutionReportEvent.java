package com.hes.zf.fix.server.message.events;

import org.springframework.context.ApplicationEvent;
import quickfix.fix44.ExecutionReport;

public class ExecutionReportEvent extends ApplicationEvent  {

    private ExecutionReport message;

    public ExecutionReport getMessage() {
        return message;
    }

    public ExecutionReportEvent(Object source, quickfix.fix44.ExecutionReport message) {
        super(source);
        this.message = message;
    }
}
