package com.itau.jingdong.bean;


public class Favorite {
    private int g_id;
    private String u_name;

    public void setG_id(int id){this.g_id = id;}
    public void setU_name(String name){this.u_name = name;}

    public Favorite(int id, String name){
        this.g_id = id;
        this.u_name = name;
    }
}
