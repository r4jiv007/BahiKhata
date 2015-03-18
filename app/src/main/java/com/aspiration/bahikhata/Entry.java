package com.aspiration.bahikhata;

/**
 * Created by abhi on 07/03/15.
 */
public class Entry {
    private Integer id;
    private String date;
    private String detail;
    private Long amount;
    private Integer accountId;

    public Entry(Integer id, String date, String detail, Long amount, Integer accountId) {
        this.id = id;
        this.date = date;
        this.detail = detail;
        this.amount = amount;
        this.accountId = accountId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
