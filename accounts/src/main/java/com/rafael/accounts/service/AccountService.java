package com.rafael.accounts.service;

import com.rafael.accounts.model.Account;
import com.rafael.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> getAccountByCustomerId(int customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

}
