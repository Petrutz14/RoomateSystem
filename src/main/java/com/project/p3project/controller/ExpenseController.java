package com.project.p3project.controller;

import com.project.p3project.model.Expense;
import com.project.p3project.service.ExpenseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private String getAuthenticatedUserEmail() {
        //Get email when request is made
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @PostMapping
    public void create(@RequestBody Expense expense) {
        String email = getAuthenticatedUserEmail();
        expenseService.createWithUser(expense, email);
    }

    @GetMapping("/apartment/{apartmentId}")
    public List<Expense> getByApartment(@PathVariable Long apartmentId) {
        String email = getAuthenticatedUserEmail();
        return expenseService.getByApartmentForUser(apartmentId, email);
    }
}