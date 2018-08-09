package com.dogymate.model;

import java.io.Serializable;

/*
Created By JEFRI SINGH
ON 27/7/18
*/
public class ChatUser implements Serializable {
    private String name = "";
    private String user_id = "";

    public ChatUser(String user_id,String name) {
        this.name = name;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
