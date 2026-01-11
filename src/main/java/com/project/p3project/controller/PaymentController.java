package com.project.p3project.controller;

import com.project.p3project.model.Payment;
import com.project.p3project.service.PaymentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @PostMapping
    public void createPayment(@RequestBody Payment payment) {
        String email = getAuthenticatedUserEmail();
        paymentService.processPayment(payment, email);
    }

    @GetMapping("/expense/{expenseId}")
    public List<Payment> getByExpense(@PathVariable Long expenseId) {
        return paymentService.getPaymentsForExpense(expenseId);
    }
}