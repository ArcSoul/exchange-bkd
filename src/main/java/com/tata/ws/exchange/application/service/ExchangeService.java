package com.tata.ws.exchange.application.service;

import com.tata.ws.exchange.application.model.request.ExchangeRateRequest;
import com.tata.ws.exchange.application.model.request.RecordExchangeRateRequest;
import com.tata.ws.exchange.application.model.response.ExchangeRateResponse;
import com.tata.ws.exchange.application.model.response.RecordExchangeRateResponse;
import com.tata.ws.exchange.application.model.response.TypeExchangeRateResponse;
import com.tata.ws.exchange.domain.model.ExchangeRate;
import com.tata.ws.exchange.infrastructure.feign.ExchangeRateClient;
import com.tata.ws.exchange.infrastructure.feign.ExchangeRateClientResponse;
import com.tata.ws.exchange.infrastructure.feign.ExchangeRateFreeClient;
import com.tata.ws.exchange.infrastructure.feign.SupportedCodesResponse;
import com.tata.ws.exchange.infrastructure.metric.ConversionMetrics;
import com.tata.ws.exchange.infrastructure.persistence.ExchangeRateJpaRepository;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRateJpaRepository exchangeRateRepository;
    private final ExchangeRateClient exchangeRateClient;
    private final ExchangeRateFreeClient exchangeRateFreeClient;
    private final ConversionMetrics conversionMetrics;

    public ExchangeRateResponse applyExchangeRate(ExchangeRateRequest request) {
        Timer.Sample timerSample = conversionMetrics.startConversionTimer();

        try {
            ExchangeRateClientResponse responseClient = exchangeRateFreeClient.getLatestExchangeRate(request.getOriginCurrency());
            Double exchangeRate = responseClient.getExchangeRateByCurrency(request.getDestinationCurrency());

            if (exchangeRate == null) {
                conversionMetrics.trackConversionError(request.getOriginCurrency(), request.getDestinationCurrency());
                throw new IllegalArgumentException("Tipo de cambio no encontrado " + request.getDestinationCurrency());
            }

            double amountWithExchangeRate = request.getAmount() * exchangeRate;

            ExchangeRate exchangeRateNotSaved = new ExchangeRate();
            exchangeRateNotSaved.setOriginCurrency(request.getOriginCurrency());
            exchangeRateNotSaved.setDestinationCurrency(request.getDestinationCurrency());
            exchangeRateNotSaved.setRate(exchangeRate);
            exchangeRateNotSaved.setOriginalMount(request.getAmount());
            exchangeRateNotSaved.setConvertMount(amountWithExchangeRate);

            exchangeRateRepository.save(exchangeRateNotSaved);

            conversionMetrics.trackConversion(request.getOriginCurrency(), request.getDestinationCurrency());

            ExchangeRateResponse response = new ExchangeRateResponse();
            response.setAmount(request.getAmount());
            response.setAmountWithExchangeRate(amountWithExchangeRate);
            response.setDestinationCurrency(request.getDestinationCurrency());
            response.setOriginCurrency(request.getOriginCurrency());
            response.setExchangeRate(exchangeRate);

            conversionMetrics.stopConversionTimer(timerSample, request.getOriginCurrency(), request.getDestinationCurrency());

            return response;
        } catch (Exception e) {
            conversionMetrics.trackConversionError(request.getOriginCurrency(), request.getDestinationCurrency());
            conversionMetrics.stopConversionTimer(timerSample, request.getOriginCurrency(), request.getDestinationCurrency());
            throw e;
        }
    }

    public List<TypeExchangeRateResponse> getExchangeRate() {
        SupportedCodesResponse responseClient = exchangeRateClient.getExchangeRate();

        return responseClient.getSupportedCodes()
                .stream()
                .map(client -> new TypeExchangeRateResponse(
                        client.getCode(),
                        client.getName())
                ).toList();
    }

    public List<RecordExchangeRateResponse> recordExchangeRate(RecordExchangeRateRequest request) {
        List<ExchangeRate> exchangeRateRecords = exchangeRateRepository.findAllByCreatedBy(request.getUsername());

        if (!exchangeRateRecords.isEmpty()) {
            List<RecordExchangeRateResponse> response = exchangeRateRecords.stream()
                    .map(exchangeRate -> new RecordExchangeRateResponse(
                            exchangeRate.getOriginalMount(),
                            exchangeRate.getConvertMount(),
                            exchangeRate.getOriginCurrency(),
                            exchangeRate.getDestinationCurrency(),
                            exchangeRate.getRate(),
                            exchangeRate.getCreatedDate()))
                    .toList();
            log.info("[ExchangeService][recordExchangeRate] response: {}", response);
            return response;
        } else {
            return List.of();
        }
    }
}
