package com.aspiration.lalookhata;

/**
 * Created by abhi on 13/02/15.
 */
public class Account {
    private String name;
    private String place;
    private Long contact;
    private Long balance;
    private Integer id;

    public Account(Integer id, String name, String place, Long contact,Long balance){
        this.id = id;
        this.name = name;
        this.place = place;
        this.contact = contact;
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }
}
