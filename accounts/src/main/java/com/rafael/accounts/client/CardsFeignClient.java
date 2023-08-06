package com.rafael.accounts.client;

import com.rafael.accounts.model.Card;
import com.rafael.accounts.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
    ResponseEntity<?> getCardDetails(@RequestHeader("rafaelbank-correlation-id") String correlationId, @RequestBody Customer customer);
}
