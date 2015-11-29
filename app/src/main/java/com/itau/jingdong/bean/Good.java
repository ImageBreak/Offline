package com.itau.jingdong.bean;


public class Good {

    private int g_id;
    private String g_name;
    private String g_price;
    private String g_pic;
    private String u_name;
    private int g_amount;
    private String g_type;

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public void setG_pic(String g_pic) {
        this.g_pic = g_pic;
    }

    public Good(String goodname,String goodprice, String goodphoto, int goodamount, String goodtype,String name) {
        this.g_name = goodname;
        this.g_price = goodprice;
        this.g_pic = goodphoto;
        this.g_amount = goodamount;
        this.g_type = goodtype;
        this.u_name = name;
    }
    public int getG_id(){return this.g_id;}
    public String getG_name(){return this.g_name;}
    public String getG_price(){return this.g_price;}
    public int getG_amount(){return this.g_amount;}
    public String getG_pic() {return this.g_pic;}

    public Good() {
        super();
        // TODO Auto-generated constructor stub
    }
}
