package com.example.payment.controller;
import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public String showPayments(
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String currency,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            Model model) {

        // Fetching available currencies dynamically
        List<String> currencies = paymentService.fetchAllCurrencies();

        Page<Payment> paymentsPage = paymentService.fetchPayments(page, size, dateFrom, dateTo, currency, pageSize, pageNumber);

        model.addAttribute("payments", paymentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentsPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", paymentsPage.getTotalElements());

        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("currency", currency);

        // Passing the list of currencies to the view
        model.addAttribute("currencies", currencies);

        return "payments";
    }


}
