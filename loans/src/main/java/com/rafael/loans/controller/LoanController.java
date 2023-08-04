package com.rafael.loans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rafael.loans.config.LoansServiceConfig;
import com.rafael.loans.model.Customer;
import com.rafael.loans.model.Loan;
import com.rafael.loans.model.Properties;
import com.rafael.loans.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoanController {

    @Autowired
    LoansServiceConfig loansConfig;
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /*
    Todo:
     - Due to the short time that I have available, many of the methods in this controller will have responsibilities
      that should be given to a Service Class. This is a bad practice and it's use is only intended to speed up boilerplate code.
     - Refactor needed in the future.
     - Business Logic is out of the scope of this project, but may be properly implemented in the future
     - This might apply to other controllers in this project.
   */
    @PostMapping("/myLoans")
    public ResponseEntity<?> getLoansDetails(@RequestBody Customer customer) {
        int customerId = customer.getCustomerId();
        List<Loan> loans = loanService.findLoansByCustomerId(customerId);
        if (loans.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No account found for customer ID: " + customerId);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(loans);
        }
    }

    @GetMapping("/loans/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
                loansConfig.getMailDetails(), loansConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }
}
