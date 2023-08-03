package com.rafael.cards.repository;

import com.rafael.cards.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends CrudRepository<Card, Long> {
    List<Card> findByCustomerId(int customerId);
}
