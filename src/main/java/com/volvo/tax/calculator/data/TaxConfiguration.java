package com.volvo.tax.calculator.data;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class TaxConfiguration {

  private String name;
  private int maxTaxAmountPerDay;
  private long singleChargeRuleMinutes;
  private Set<LocalDate> publicHolidays;
  private Set<DayOfWeek> weekends;
  private Set<String> taxFreeMonths;
  private Set<String> taxFreeVehicleTypes;
  private Set<TimeRule> taxPeriods;
}
