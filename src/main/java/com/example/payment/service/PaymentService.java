package com.example.payment.service;

import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public Page<Payment> fetchPayments(int page, int size, String dateFrom, String dateTo, String currency) {
        List<Payment> payments = repository.getAllPayments(dateFrom, dateTo, currency);

        if (payments.isEmpty()) {
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
        }

        int start = page * size;
        int end = Math.min(start + size, payments.size());

        if (start >= payments.size()) {
            return new PageImpl<>(List.of(), PageRequest.of(page, size), payments.size());
        }

        List<Payment> sublist = payments.subList(start, end);
        return new PageImpl<>(sublist, PageRequest.of(page, size), payments.size());
    }

    public List<String> fetchAllCurrencies() {
        return repository.getAllCurrencies();
    }

}
