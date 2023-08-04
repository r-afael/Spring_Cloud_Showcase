package com.rafael.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rafael.accounts.config.AccountsServiceConfig;
import com.rafael.accounts.model.Account;
import com.rafael.accounts.model.Customer;
import com.rafael.accounts.model.Properties;
import com.rafael.accounts.repository.AccountRepository;
import com.rafael.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AccountsController {

    @Autowired
    AccountsServiceConfig accountsConfig;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    @Autowired
    public AccountsController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    /*
      Todo:
        - Due to the short time that I have available, many of the methods in this controller will have responsibilities
        that should be given to a Service Class. This is a bad practice and it's use is only intended to speed up boilerplate code.
        - Refactor needed in the future.
        - Business Logic is out of the scope of this project, but may be properly implemented in the future
        - This might apply to other controllers in this project.
     */
    @GetMapping("/myAccount")
    public ResponseEntity<?> getAccountByCustomerId(@RequestBody Customer customer) {
        int customerId = customer.getCustomerId();
        Optional<Account> accountOpt = accountService.getAccountByCustomerId(customerId);

        if (accountOpt.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No account found for customer ID: " + customerId);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(accountOpt.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
                accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }
}
