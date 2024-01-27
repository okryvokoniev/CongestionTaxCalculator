package com.volvo.tax.calculator.service;

import com.volvo.tax.calculator.dto.CongestionTaxRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CongestionTaxCalculatorServiceImplIntegrationTest {

    @Autowired
    private CongestionTaxCalculatorService service;

    @Test
    void getTaxAmountSuccess() {
        CongestionTaxRequest request = CongestionTaxRequest.builder()
                .city("GOTHENBURG")
                .vehicleType("CAR")
                .dates(List.of(LocalDateTime.of(2013, 2, 8, 6, 20, 27),
                        LocalDateTime.of(2013, 2, 8, 6, 27, 0),
                        LocalDateTime.of(2013, 2, 8, 7, 21, 0),
                        LocalDateTime.of(2013, 2, 8, 7, 27, 0),
                        LocalDateTime.of(2013, 2, 8, 8, 5, 0),
                        LocalDateTime.of(2013, 2, 11, 6, 27, 0),
                        LocalDateTime.of(2013, 2, 11, 7, 21, 0),
                        LocalDateTime.of(2013, 2, 11, 8, 27, 0),
                        LocalDateTime.of(2013, 2, 11, 9, 5, 0),
                        LocalDateTime.of(2013, 2, 11, 10, 27, 0),
                        LocalDateTime.of(2013, 2, 11, 11, 28, 0),
                        LocalDateTime.of(2013, 2, 11, 12, 29, 0),
                        LocalDateTime.of(2013, 2, 11, 15, 30, 0),
                        LocalDateTime.of(2013, 2, 11, 16, 30, 0)))
                .build();

        int result = service.getTaxAmount(request);

        assertEquals(86, result);
    }

    @Test
    void getTaxAmountConfigurationIsNotFound() {
        CongestionTaxRequest request = CongestionTaxRequest.builder()
                .city("MALMO")
                .vehicleType("CAR")
                .dates(List.of(LocalDateTime.of(2013, 2, 8, 6, 27, 0)))
                .build();

        int result = service.getTaxAmount(request);

        assertEquals(0, result);
    }

    @Test
    void getTaxAmountConfigurationFreeTaxForBus() {
        CongestionTaxRequest request = CongestionTaxRequest.builder()
                .city("GOTHENBURG")
                .vehicleType("BUS")
                .dates(List.of(LocalDateTime.of(2013, 2, 8, 6, 27, 0)))
                .build();

        int result = service.getTaxAmount(request);

        assertEquals(0, result);
    }

}
