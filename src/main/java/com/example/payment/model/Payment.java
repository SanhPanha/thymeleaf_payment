package com.example.payment.model;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class Payment {
    private Long id;
    private String acleda;
    private String name;
    private Date paidDate;
    private Timestamp paidTime;
    private Double amount;
    private String currency;
    private String referceNo;
}
