package com.windcoder.common.utills;



import org.json.JSONObject;

import java.io.Serializable;

/**
 * 结果工具类
 * Created by wind on 2016/6/15.
 * http://windcoder.com
 */
public class ReturnResult implements Serializable {
    private int code = 0;
    private String msg = null;
    private Object result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String toJsonString(){

        JSONObject json = new JSONObject(this);
        return json.toString();
    }
}
