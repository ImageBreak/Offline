package com.itau.jingdong.bean;


import java.util.Date;

public class Trade {
    private int g_id;
    private String u_name;
    private String t_color;
    private String t_type;
    private int t_count;
    private int g_amount;
    private String t_time;
    private String g_name;
    private String g_price;
    private String g_pic;

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public String getT_color() {
        return t_color;
    }

    public String getT_type() {
        return t_type;
    }

    public String getT_time() {
        return t_time;
    }

    public int getT_count() {
        return t_count;
    }

    public String getG_pic() {
        return g_pic;
    }

    public int getG_id() {
        return g_id;
    }

    public String getG_name() {
        return g_name;
    }

    public String getG_price() {
        return g_price;
    }

    public int getG_amount() {
        return g_amount;
    }

    public void setG_pic(String g_pic) {
        this.g_pic = g_pic;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public void setT_count(int t_count) {
        this.t_count = t_count;
    }

    public void setT_type(String t_type) {
        this.t_type = t_type;
    }

    public void setT_color(String t_color) {
        this.t_color = t_color;
    }

    public Trade(String name, String color, String type, int count){
        this.u_name = name;
        this.t_color = color;
        this.t_type = type;
        this.t_count = count;
        Date date = new Date();
        String time = String.format("%tF",date)+" "+String.format("%tT",date);
        this.t_time = time;
    }

    public Trade(){
        Date date = new Date();
        String time = String.format("%tF",date)+" "+String.format("%tT",date);
        this.t_time = time;
    }
}
