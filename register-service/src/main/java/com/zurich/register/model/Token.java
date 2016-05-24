package com.zurich.register.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Token {

    private String userId;

    @Id
    @GeneratedValue
    private int id;

    Token() {
    }

    public Token(String userId, int token) {
        this.userId = userId;
        this.id = token;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }
}