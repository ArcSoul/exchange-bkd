package com.tata.ws.exchange.infrastructure.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversionMetrics {
    private final MeterRegistry meterRegistry;

    public void trackConversion(String baseCurrency, String targetCurrency) {
        meterRegistry.counter("currency.conversions", "baseCurrency", baseCurrency, "targetCurrency", targetCurrency).increment();
    }

    public void trackConversionError(String originCurrency, String destinationCurrency) {
        meterRegistry.counter("currency.conversions.errors", "baseCurrency", originCurrency, "targetCurrency", destinationCurrency).increment();
    }

    public Timer.Sample startConversionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopConversionTimer(Timer.Sample sample, String originCurrency, String destinationCurrency) {
        sample.stop(meterRegistry.timer("currency.conversions.duration", "baseCurrency", originCurrency, "targetCurrency", destinationCurrency));
    }
}
