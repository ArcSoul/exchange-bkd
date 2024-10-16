package com.tata.ws.exchange.domain.repository;

import com.tata.ws.exchange.domain.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findAllByCreatedBy(String username);
}
