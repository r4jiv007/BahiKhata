package com.aspiration.lalookhata;

/**
 * Created by abhi on 13/02/15.
 */
public class Account {
    private String name;
    private Integer balance;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account(Integer id,String name, Integer balance){
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
