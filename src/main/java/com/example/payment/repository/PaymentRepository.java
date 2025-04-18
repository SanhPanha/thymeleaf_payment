package com.example.payment.repository;

import com.example.payment.model.Payment;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Payment> getAllPayments(String dateFrom, String dateTo, String currency, int pageSize, int pageNumber) {
        return jdbcTemplate.execute((Connection conn) -> {
            CallableStatement cs = conn.prepareCall("{call GET_PAYMENTS_PAGINATION(?, ?, ?, ?, ?)}");

            // Set p_date_from
            if (dateFrom != null && !dateFrom.isEmpty()) {
                java.sql.Date sqlDateFrom = java.sql.Date.valueOf(dateFrom); // format: yyyy-MM-dd
                cs.setDate(1, sqlDateFrom);
            } else {
                cs.setNull(1, Types.DATE);
            }

            // Set p_date_to
            if (dateTo != null && !dateTo.isEmpty()) {
                java.sql.Date sqlDateTo = java.sql.Date.valueOf(dateTo); // format: yyyy-MM-dd
                cs.setDate(2, sqlDateTo);
            } else {
                cs.setNull(2, Types.DATE);
            }

            // Set p_currency
            if (currency != null && !currency.isEmpty()) {
                cs.setString(3, currency);
            } else {
                cs.setNull(3, Types.VARCHAR);
            }

            cs.registerOutParameter(4, OracleTypes.CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(4);
            List<Payment> payments = new ArrayList<>();

            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getLong("ID"));
                p.setAcleda(rs.getString("ACLEDA"));
                p.setName(rs.getString("NAME"));
                p.setPaidDate(rs.getDate("PAID_DATE"));
                p.setPaidTime(rs.getTimestamp("PAID_TIME"));
                p.setAmount(rs.getDouble("AMOUNT"));
                p.setCurrency(rs.getString("CURRENCY"));
                p.setReferceNo(rs.getString("REFERCE_NO"));
                payments.add(p);
            }

            return payments;
        });
    }

    public List<String> getAllCurrencies() {
        String sql = "SELECT DISTINCT CURRENCY FROM PAYMENT";

        return jdbcTemplate.queryForList(sql, String.class);
    }
}
