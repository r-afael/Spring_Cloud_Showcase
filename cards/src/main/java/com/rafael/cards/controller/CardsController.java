package com.rafael.cards.controller;

import com.rafael.cards.config.CardsServiceConfig;
import com.rafael.cards.model.Card;
import com.rafael.cards.model.Customer;
import com.rafael.cards.model.Properties;
import com.rafael.cards.repository.CardsRepository;
import com.rafael.cards.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CardsController {

    //Todo: Remove field injection of the repository from the controller
    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    CardsServiceConfig cardsConfig;
    private CardsService cardsService;

    @Autowired
    public CardsController(CardsService cardsService) {this.cardsService = cardsService;}

    /*
      Todo:
        - Due to the short time that I have available, many of the methods in this controller will have responsibilities
        that should be given to a Service Class. This is a bad practice and it's use is only intended to speed up boilerplate code.
        - Refactor needed in the future.
        - Business Logic is out of the scope of this project, but may be properly implemented in the future
        - This might apply to other controllers in this project.
     */
    @PostMapping("/myCards")
    public ResponseEntity<?> getCardDetails(@RequestBody Customer customer) {
        int customerId = customer.getCustomerId();
        List<Card> cards = cardsService.getCardsByCustomerId(customerId);

        if (cards.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No cards found for customer ID: " + customerId);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(cards, HttpStatus.OK);
        }
    }

    @GetMapping("/cards/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(),
                cardsConfig.getMailDetails(), cardsConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }
}
