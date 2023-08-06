package com.rafael.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rafael.accounts.config.AccountsServiceConfig;
import com.rafael.accounts.model.Account;
import com.rafael.accounts.model.Customer;
import com.rafael.accounts.model.CustomerDetails;
import com.rafael.accounts.model.Properties;
import com.rafael.accounts.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountsController {
    private final AccountsServiceConfig accountsConfig;
    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService,  AccountsServiceConfig accountsConfig) {
        this.accountService = accountService;
        this.accountsConfig = accountsConfig;
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
        try {
            Account account = accountService.getAccountByCustomerId(customer.getCustomerId());
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (PropertyNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    //Gets remote env properties set on another repository
    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
                accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }

    @PostMapping("/myCustomerDetails")
    /*@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")*/
    @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "customerDetailsFallback")
    public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
        //Todo: return a ResponseEntity and add try catch
        return accountService.getCustomerDetails(customer);
    }

    //Tests Rate Limiter to protect APIs from too many requests, you can try this by refreshing the page multiple times
    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, this is the standard return";
    }

    //Fallback method called from the Circuit Breaker
    private CustomerDetails customerDetailsFallback(Customer customer, Throwable t) {
        return accountService.customerDetailsFallback(customer, t);
    }

    //Fallback method called from the Rate Limiter
    private String sayHelloFallback(Throwable t) {
        return "Hi, this is the rate limiter fallback";
    }
}
