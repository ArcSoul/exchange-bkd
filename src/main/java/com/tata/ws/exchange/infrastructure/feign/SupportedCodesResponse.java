package com.tata.ws.exchange.infrastructure.feign;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class SupportedCodesResponse {
    private String result;
    @JsonProperty("supported_codes")
    private List<CurrencyCode> supportedCodes;

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class CurrencyCode {
        private String code;
        private String name;

        @JsonCreator
        public CurrencyCode(String[] codeAndName) {
            this.code = codeAndName[0];
            this.name = codeAndName[1];
        }
    }
}
