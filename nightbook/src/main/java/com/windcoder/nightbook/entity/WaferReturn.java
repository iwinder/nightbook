package com.windcoder.nightbook.entity;

import org.json.JSONObject;

/**
 * Created by wind on 2016/12/24.
 */
public class WaferReturn {
    private Integer returnCode;
    private String returnMessage;
    private String returnData;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public String toJsonString(){

        JSONObject json = new JSONObject(this);
        return json.toString();
    }
}
