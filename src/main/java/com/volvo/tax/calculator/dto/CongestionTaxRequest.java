package com.volvo.tax.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CongestionTaxRequest {
    @NotNull
    private String city;
    @NotNull
    private String vehicleType;
    @NotNull
    private List<LocalDateTime> dates;
}
