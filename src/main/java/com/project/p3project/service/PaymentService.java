package com.project.p3project.service;

import com.project.p3project.dao.ExpenseDAO;
import com.project.p3project.dao.PaymentDAO;
import com.project.p3project.dao.UserDAO;
import com.project.p3project.model.Expense;
import com.project.p3project.model.Payment;
import com.project.p3project.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentDAO paymentDao;
    private final ExpenseDAO expenseDao;
    private final UserDAO userDao;

    public PaymentService(PaymentDAO paymentDao, ExpenseDAO expenseDao, UserDAO userDao) {
        this.paymentDao = paymentDao;
        this.expenseDao = expenseDao;
        this.userDao = userDao;
    }

    @Transactional
    public void processPayment(Payment payment, String userEmail) {
        //Identify the paying user
        User user = userDao.findByEmail(userEmail);
        payment.setUserId(user.getId());

        //Bill details
        Expense expense = expenseDao.findById(payment.getExpenseId());
        if (expense == null) {
            throw new IllegalArgumentException("Expense not found.");
        }

        if (payment.getAmount().compareTo(expense.getAmount()) > 0) {
            throw new IllegalArgumentException("Payment exceeds current balance of: " + expense.getAmount());
        }

        //Set date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        paymentDao.save(payment);

        //Reduce the ammount
        expenseDao.subtractFromBalance(payment.getExpenseId(), payment.getAmount());
    }

    public List<Payment> getPaymentsForExpense(Long expenseId) {
        return paymentDao.findByExpenseId(expenseId);
    }
}
