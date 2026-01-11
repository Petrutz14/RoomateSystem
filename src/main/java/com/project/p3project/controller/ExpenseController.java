package com.project.p3project.controller;

import com.project.p3project.model.Expense;
import com.project.p3project.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public void create(@RequestBody Expense expense) {
        expenseService.create(expense);
    }

    @GetMapping("/{id}")
    public Expense getById(@PathVariable Long id) {
        return expenseService.getById(id);
    }

    @GetMapping("/apartment/{apartmentId}")
    public List<Expense> getByApartment(@PathVariable Long apartmentId) {
        return expenseService.getByApartmentId(apartmentId);
    }

    @GetMapping
    public List<Expense> getAll() {
        return expenseService.getAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Expense expense) {
        expenseService.update(id, expense);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }
}