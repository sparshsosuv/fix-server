package com.hes.zf.fix.server.infrastructure;

import java.util.HashMap;
import java.util.Map;

public enum Operation {

    BUY(1), SELL(2), BUY_MINUS(3), SELL_PLUS(4), SELL_SHORT(5), SELL_SHORT_EXEMPT(6), UNDISCLOSES(7), CROSS(8), CROSS_SHORT(9), TRADED(99), BOUGHT(98), SOLD(97);

    private Integer value;

    private static Map<Integer, Operation> map = new HashMap<>();

    Operation(Integer value) {
        this.value = value;
    }

    static {
        for (Operation operation : Operation.values()) {
            map.put(operation.value, operation);
        }
    }

    public static Operation valueOf(int operation) {
        return map.get(operation);
    }

}
