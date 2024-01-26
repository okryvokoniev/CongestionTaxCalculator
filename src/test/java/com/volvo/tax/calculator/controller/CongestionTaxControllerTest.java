package com.volvo.tax.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.tax.calculator.dto.CongestionTaxRequest;
import com.volvo.tax.calculator.service.CongestionTaxCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CongestionTaxController.class)
class CongestionTaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CongestionTaxCalculatorService service;

    @Test
    void testGetTaxSuccess() throws Exception {
        CongestionTaxRequest request = CongestionTaxRequest.builder()
                .city("MALMO")
                .vehicleType("CAR")
                .dates(List.of(LocalDateTime.of(2013, 2, 8, 6, 27, 0)))
                .build();

        when(service.getTaxAmount(request)).thenReturn(10);

        mockMvc.perform(post("/api/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taxAmount").value(10));
    }

    @Test
    void testGetTaxCityIsNullBadRequest() throws Exception {
        CongestionTaxRequest request = CongestionTaxRequest.builder()
                .city(null)
                .vehicleType("CAR")
                .dates(List.of(LocalDateTime.of(2013, 2, 8, 6, 27, 0)))
                .build();

        when(service.getTaxAmount(request)).thenReturn(10);

        mockMvc.perform(post("/api/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}

