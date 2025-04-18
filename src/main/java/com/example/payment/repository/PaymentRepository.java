package com.example.payment.repository;

import com.example.payment.model.Payment;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Payment> getPaymentsByDate(String dateFrom, String dateTo) {
        return jdbcTemplate.execute((Connection conn) -> {
            CallableStatement cs;

            if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
                cs = conn.prepareCall("{call GET_PAYMENTS_BY_DATE(?, ?, ?)}");
                cs.setString(1, dateFrom);
                cs.setString(2, dateTo);
                cs.registerOutParameter(3, OracleTypes.CURSOR);
                cs.execute();

                ResultSet rs = (ResultSet) cs.getObject(3);
                return mapResultSet(rs);
            } else {
                cs = conn.prepareCall("{call GET_ALL_PAYMENTS(?)}");
                cs.registerOutParameter(1, OracleTypes.CURSOR);
                cs.execute();

                ResultSet rs = (ResultSet) cs.getObject(1);
                return mapResultSet(rs);
            }
        });
    }

    private List<Payment> mapResultSet(ResultSet rs) throws SQLException {
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
    }

}
