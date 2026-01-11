package com.project.p3project.service;

import com.project.p3project.dao.ApartmentDAO;
import com.project.p3project.dao.ExpenseDAO;
import com.project.p3project.dao.UserDAO;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.model.Expense;
import com.project.p3project.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseDAO expenseDao;
    private final UserDAO userDao;
    private final ApartmentDAO apartmentDao;

    public ExpenseService(ExpenseDAO expenseDao, UserDAO userDao, ApartmentDAO apartmentDao) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.apartmentDao = apartmentDao;
    }

    public void createWithUser(Expense expense, String email) {
        User user = userDao.findByEmail(email);
        //Set the userId on the expense object
        expense.setUserId(user.getId());
        validate(expense);
        expenseDao.save(expense);
    }


    public List<Expense> getByApartmentForUser(Long apartmentId, String email) {
        return expenseDao.findByApartmentAndUser(apartmentId, email);
    }

    //public List<Expense> getAll() {
    //    return expenseDao.findAll();
    //}

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
            throw new BlankDataException("Expense date");
        }
    }
}