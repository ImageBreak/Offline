package com.itau.jingdong.bean;


import java.util.Date;

public class Evaluation {
    private int g_id;
    private String u_name;
    private String e_text;
    private String e_time;
    private String u_pic;

    public int getG_id() {
        return g_id;
    }

    public String getU_name() {
        return u_name;
    }

    public String getE_text() {
        return e_text;
    }

    public String getE_time(){return e_time;}

    public String getU_pic() {
        return u_pic;
    }

    public Evaluation(int goodid,String name,String text){
        this.g_id = goodid;
        this.u_name = name;
        this.e_text = text;
        Date date = new Date();
        String time = String.format("%tF",date)+" "+String.format("%tT",date);
        this.e_time = time;
    }

}
