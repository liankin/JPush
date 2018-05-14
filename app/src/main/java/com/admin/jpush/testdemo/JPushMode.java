package com.admin.jpush.testdemo;

import com.admin.jpush.testdemo.JsonRespondParse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/20 0020.
 */
@HttpResponse(parser = JsonRespondParse.class)
public class JPushMode implements Serializable{

    /**
     * typename : null
     * num : 17062000009
     * type : 1
     */
    private boolean debug;
    private Object typename;
    private String num;
    private int type;
    private String ip;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getTypename() {
        return typename;
    }

    public void setTypename(Object typename) {
        this.typename = typename;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
