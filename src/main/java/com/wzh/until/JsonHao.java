package com.wzh.until;

public class JsonHao {
  private  String code;
  private  String msg,hao;

    public String getHao() {
        return hao;
    }

    public void setHao(String hao) {
        this.hao = hao;
    }

    private  Object data;

    public String getCode() {
        return code;
    }

    public JsonHao(String code, String msg, String hao, Object data) {
        this.code = code;
        this.msg = msg;
        this.hao = hao;
        this.data = data;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
