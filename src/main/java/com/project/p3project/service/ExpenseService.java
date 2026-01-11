package com.project.p3project.service;

import com.project.p3project.dao.ApartmentDao;
import com.project.p3project.dao.ExpenseDao;
import com.project.p3project.dao.UserDao;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.model.Expense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseDao expenseDao;
    private final UserDao userDao;
    private final ApartmentDao apartmentDao;

    public ExpenseService(ExpenseDao expenseDao, UserDao userDao, ApartmentDao apartmentDao) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.apartmentDao = apartmentDao;
    }

    public void create(Expense expense) {
        validate(expense);
        if (userDao.findById(expense.getUserId()) == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (apartmentDao.findById(expense.getApartmentId()) == null) {
            throw new IllegalArgumentException("Apartment does not exist");
        }
        expenseDao.save(expense);
    }

    public Expense getById(Long id) {
        Expense expense = expenseDao.findById(id);
        if (expense == null) {
            throw new IllegalArgumentException("Expense not found");
        }
        return expense;
    }

    public List<Expense> getByApartmentId(Long apartmentId) {
        return expenseDao.findByApartmentId(apartmentId);
    }

    public List<Expense> getAll() {
        return expenseDao.findAll();
    }

    public void update(Long id, Expense expense) {
        validate(expense);
        if (expenseDao.findById(id) == null) {
            throw new IllegalArgumentException("Expense not found");
        }
        expenseDao.update(id, expense);
    }

    public void delete(Long id) {
        if (expenseDao.deleteById(id) == 0) {
            throw new IllegalArgumentException("Expense not found");
        }
    }

    public void validate(Expense expense) {
        if (expense == null) {
            throw new BlankDataException("Expense data");
        }

        if (expense.getTitle() == null || expense.getTitle().isBlank()) {
            throw new BlankDataException("Expense title");
        }

        if (expense.getCategory() == null || expense.getCategory().isBlank()) {
            throw new BlankDataException("Category");
        }

        if (expense.getAmount() == null) {
            throw new BlankDataException("Amount");
        }
        // Check if amount is less than or equal to zero
        if (expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number.");
        }

        if (expense.getUserId() == null || expense.getUserId() <= 0) {
            throw new BlankDataException("User ID");
        }

        if (expense.getApartmentId() == null || expense.getApartmentId() <= 0) {
            throw new BlankDataException("Apartment ID");
        }

        if (expense.getExpenseDate() == null) {
            throw new BlankDataException("Expense date is required.");
        }
    }
}