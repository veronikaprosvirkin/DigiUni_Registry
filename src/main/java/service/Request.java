package service;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private Object data;

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }
    public Request(String action) {
        this.action = action;
        this.data = null;
    }

    public String getAction() { return action; }
    public Object getData() { return data; }
}