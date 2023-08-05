package com.rafael.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rafael.accounts.config.AccountsServiceConfig;
import com.rafael.accounts.model.*;
import com.rafael.accounts.repository.AccountRepository;
import com.rafael.accounts.service.AccountService;
import com.rafael.accounts.client.CardsFeignClient;
import com.rafael.accounts.client.LoansFeignClient;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AccountsController {
    private final AccountsServiceConfig accountsConfig;
    private final LoansFeignClient loansFeignClient;
    private final CardsFeignClient cardsFeignClient;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountsController(AccountService accountService, AccountRepository accountRepository,  AccountsServiceConfig accountsConfig,
                              LoansFeignClient loansFeignClient, CardsFeignClient cardsFeignClient) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.accountsConfig = accountsConfig;
        this.loansFeignClient = loansFeignClient;
        this.cardsFeignClient = cardsFeignClient;
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

    @PostMapping("/myCustomerDetails")
    public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
        //Todo: Move relevant code to service class and return a ResponseEntity
        Optional<Account> accountOpt = accountRepository.findByCustomerId(customer.getCustomerId());
        if (accountOpt.isEmpty()) {
            throw new PropertyNotFoundException("No account for customer with id: " + customer.getCustomerId());
        }
        Account account = accountOpt.get();

        ResponseEntity<?> loansResponse = loansFeignClient.getLoansDetails(customer);
        validateResponse(loansResponse);
        List<Loan> loans = (List<Loan>) loansResponse.getBody();

        ResponseEntity<?> cardsResponse = cardsFeignClient.getCardDetails(customer);
        validateResponse(cardsResponse);
        List<Card> cards = (List<Card>) cardsResponse.getBody();

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);

        return customerDetails;
    }

    private void validateResponse(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> errorResponse = (Map<String, String>) response.getBody();
            String errorMessage = errorResponse.get("message");
            throw new PropertyNotFoundException(errorMessage);
        }
    }
}
