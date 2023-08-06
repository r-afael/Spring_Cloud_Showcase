package com.rafael.accounts.service;

import com.rafael.accounts.client.CardsFeignClient;
import com.rafael.accounts.client.LoansFeignClient;
import com.rafael.accounts.model.*;
import com.rafael.accounts.repository.AccountRepository;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final LoansFeignClient loansFeignClient;
    private final CardsFeignClient cardsFeignClient;

    @Autowired
    public AccountService(AccountRepository accountRepository, LoansFeignClient loansFeignClient, CardsFeignClient cardsFeignClient) {
        this.accountRepository = accountRepository;
        this.loansFeignClient = loansFeignClient;
        this.cardsFeignClient = cardsFeignClient;
    }

    public Account getAccountByCustomerId(int customerId) {
        Optional<Account> accountOpt = accountRepository.findByCustomerId(customerId);
        return accountOpt.orElseThrow(() -> new PropertyNotFoundException("No account for customer with id: " + customerId));
    }

    //Makes request to the Loads Service using Feign
    public List<Loan> getLoansDetails(Customer customer) {
        ResponseEntity<?> loansResponse = loansFeignClient.getLoansDetails(customer);
        validateResponse(loansResponse);
        return (List<Loan>) loansResponse.getBody();
    }

    //Makes request to the Cards Service using Feign
    public List<Card> getCardsDetails(Customer customer) {
        ResponseEntity<?> cardsResponse = cardsFeignClient.getCardDetails(customer);
        validateResponse(cardsResponse);
        return (List<Card>) cardsResponse.getBody();
    }

    public CustomerDetails getCustomerDetails(Customer customer) {
        Account account = getAccountByCustomerId(customer.getCustomerId());
        List<Loan> loans = getLoansDetails(customer);
        List<Card> cards = getCardsDetails(customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);

        return customerDetails;
    }

    //Dummy method to test a fallback for when Cards Service is not available, making calls only to Loans Service
    public CustomerDetails customerDetailsFallback(Customer customer, Throwable t) {
        Account account = this.getAccountByCustomerId(customer.getCustomerId());
        List<Loan> loans = this.getLoansDetails(customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccount(account);
        customerDetails.setLoans(loans);

        return customerDetails;
    }

    //Validates successful response
    private void validateResponse(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> errorResponse = (Map<String, String>) response.getBody();
            String errorMessage = errorResponse.get("message");
            throw new PropertyNotFoundException(errorMessage);
        }
    }

}
