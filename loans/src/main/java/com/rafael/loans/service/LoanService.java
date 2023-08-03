package com.rafael.loans.service;

import com.rafael.loans.model.Loan;
import com.rafael.loans.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> findLoansByCustomerId(int customerId) {
        return loanRepository.findByCustomerIdOrderByStartDtDesc(customerId);
    }
}
