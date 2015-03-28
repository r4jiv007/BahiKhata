package com.aspiration.bahikhata;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by abhi on 27/03/15.
 */
public class Tx {
    Date dateTime;
    String detail;
    String type;
    Double amount;

    public Tx(Date dateTime, String detail, String type, Double amount) {
        this.dateTime = dateTime;
        this.detail = detail;
        this.type = type;
        this.amount = amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
