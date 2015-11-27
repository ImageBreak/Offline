package com.itau.jingdong.bean;

public class User {

	private String u_name;
	private String u_pass;
	private String u_repass;
	private String u_pay;
	private String u_pic;

	public String getU_name() {
		return u_name;
	}


	public User(String username,String photo, String pay, String password, String repassword) {
		this.u_name = username;
		this.u_pass = password;
		this.u_pay = pay;
		this.u_pic = photo;
		this.u_repass = repassword;
	}
	public User(String username,String password) {
		this.u_name = username;
		this.u_pass = password;
		// TODO Auto-generated constructor stub
	}

 
}
