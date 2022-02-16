package com.hylee.limitless.domain.cardset;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardSetRepository extends CrudRepository<CardSet, Long> {

    List<CardSet> findAll();

    Optional<CardSet> findById(Long id);

    CardSet save(CardSet cardSet);

    void deleteById(Long id);
}
