package com.rafael.cards.service;

import com.rafael.cards.model.Card;
import com.rafael.cards.repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardsService {

    private final CardsRepository cardsRepository;

    @Autowired
    public CardsService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    public List<Card> getCardsByCustomerId(int customerId) {
        return cardsRepository.findByCustomerId(customerId);
    }

}
