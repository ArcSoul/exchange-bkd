package com.tata.ws.exchange.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class ExchangeRate extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originCurrency;
    private String destinationCurrency;
    private double rate;
    private double originalMount;
    private double convertMount;
}
