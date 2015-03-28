package com.aspiration.bahikhata;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by abhi on 28/03/15.
 */
@ParseClassName("Transaction")
public class Transaction extends ParseObject {
    public Date getDateTime() {
        return getDate("dateTime");
    }

    public void setDateTime(Date dateTime) {
        put("dateTime",dateTime);
    }

    public String getDetail() {
        return getString("detail");
    }

    public void setDetail(String detail) {
        put("detail",detail);
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type",type);
    }

    public Double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(Double amount) {
        put("amount",amount);
    }

}
