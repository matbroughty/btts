package com.broughty.util;

/**
 * Created by matbroughty on 04/01/14.
 */
public enum ResultEnum {

    WAITING(0, BTTSHelper.WAITING),
    FAIL(1, BTTSHelper.FAIL),
    SUCCESS(2, BTTSHelper.SUCCESS);
    int status;
    String symbol;

    ResultEnum(int status, String symbol) {
        this.setStatus(status);
        this.setSymbol(symbol);
    }

    public static ResultEnum fromBoolean(Boolean result){
        return result == null ? WAITING : (result ? SUCCESS : FAIL);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
