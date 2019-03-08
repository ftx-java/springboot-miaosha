package com.tx.springboot.pojo;

import java.util.Date;

public class User {
    private Integer id;

    private String email;

    private String passworld;

    private String uername;

    private String role;

    private Integer status;

    private Date regtime;

    private String regip;

    public User(Integer id, String email, String passworld, String uername, String role, Integer status, Date regtime, String regip) {
        this.id = id;
        this.email = email;
        this.passworld = passworld;
        this.uername = uername;
        this.role = role;
        this.status = status;
        this.regtime = regtime;
        this.regip = regip;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassworld() {
        return passworld;
    }

    public void setPassworld(String passworld) {
        this.passworld = passworld == null ? null : passworld.trim();
    }

    public String getUername() {
        return uername;
    }

    public void setUername(String uername) {
        this.uername = uername == null ? null : uername.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    public String getRegip() {
        return regip;
    }

    public void setRegip(String regip) {
        this.regip = regip == null ? null : regip.trim();
    }
}