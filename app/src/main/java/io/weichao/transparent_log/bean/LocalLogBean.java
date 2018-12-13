package io.weichao.transparent_log.bean;

public class LocalLogBean {
    private int level;
    private String msg;

    public LocalLogBean() {
    }

    public LocalLogBean(int level, String msg) {
        this.level = level;
        this.msg = msg;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}