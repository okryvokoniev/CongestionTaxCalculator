package com.volvo.tax.calculator.data;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class TimeRule {
    private LocalTime leftLimit;
    private LocalTime rightLimit;
    private int tax;
}
