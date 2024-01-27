package com.volvo.tax.calculator.service;

import com.volvo.tax.calculator.data.TaxConfiguration;
import com.volvo.tax.calculator.data.TimeRule;
import com.volvo.tax.calculator.dto.CongestionTaxRequest;
import com.volvo.tax.calculator.repository.TaxConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CongestionTaxCalculatorServiceImpl implements CongestionTaxCalculatorService {

    private final TaxConfigurationRepository taxConfigurationRepository;

    @Override
    public int getTaxAmount(CongestionTaxRequest request) {
        TaxConfiguration taxConfiguration = taxConfigurationRepository.getTaxConfigurationByCity(request.getCity());

        if (taxConfiguration == null || isTaxFreeVehicle(taxConfiguration, request) || request.getDates().size() == 0) {
            return 0;
        }

        List<LocalDateTime> taxableDates = request.getDates().stream()
                .filter(checkpoint -> isPayableDate(taxConfiguration, checkpoint))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(taxableDates)) {
            return 0;
        }
        Collections.sort(taxableDates);
        return calculateTaxAmount(taxConfiguration, taxableDates);
    }

    private boolean isPayableDate(TaxConfiguration configuration, LocalDateTime checkpoint) {
        return !configuration.getWeekends().contains(checkpoint.getDayOfWeek())
                && !configuration.getPublicHolidays().contains(checkpoint.toLocalDate())
                && !configuration.getTaxFreeMonths().contains(checkpoint.getMonth().name().toUpperCase());
    }

    private int calculateTaxAmount(TaxConfiguration configuration, List<LocalDateTime> tollCheckpoints) {
        Map<LocalDateTime, Set<LocalDateTime>> groupedDatesByHour = getGroupedDatesByHour(configuration, tollCheckpoints);
        return calculateTaxAmount(configuration, groupedDatesByHour);
    }

    private Map<LocalDateTime, Set<LocalDateTime>> getGroupedDatesByHour(TaxConfiguration configuration,
                                                                         List<LocalDateTime> dates) {
        Map<LocalDateTime, Set<LocalDateTime>> groupedDatesByHour = new HashMap<>();
        LocalDateTime keyDate = dates.get(0).plusMinutes(configuration.getSingleChargeRuleMinutes());
        groupedDatesByHour.put(keyDate, new HashSet<>());
        groupedDatesByHour.get(keyDate).add(dates.get(0));

        for (LocalDateTime date : dates) {
            if (date.isAfter(keyDate)) {
                keyDate = date.plusMinutes(configuration.getSingleChargeRuleMinutes());
                groupedDatesByHour.put(keyDate, new HashSet<>());
            }
            groupedDatesByHour.get(keyDate).add(date);
        }
        return groupedDatesByHour;
    }

    private int calculateTaxAmount(TaxConfiguration configuration,
                                   Map<LocalDateTime, Set<LocalDateTime>> groupedDates) {
        Map<LocalDate, Integer> resultPerDay = new HashMap<>();
        for (Map.Entry<LocalDateTime, Set<LocalDateTime>> entry : groupedDates.entrySet()) {
            int maxPerHour = entry.getValue().stream()
                    .mapToInt(date -> getTaxAmount(date, configuration.getTaxPeriods()))
                    .max()
                    .orElse(0);

            Integer value = maxPerHour;
            if (resultPerDay.containsKey(entry.getKey().toLocalDate())) {
                Integer sum = resultPerDay.get(entry.getKey().toLocalDate()) + maxPerHour;
                value =  sum > configuration.getMaxTaxAmountPerDay() ? configuration.getMaxTaxAmountPerDay() : sum;
            }
            resultPerDay.put(entry.getKey().toLocalDate(), value);
        }
        return resultPerDay.values().stream().mapToInt(Integer::intValue).sum();
    }

    private int getTaxAmount(LocalDateTime date, Set<TimeRule> timeRules) {
        return timeRules.stream()
                .filter(timeRule -> belongsToTimeRule(date.toLocalTime(), timeRule))
                .map(TimeRule::getTax)
                .findFirst()
                .orElse(0);
    }

    private boolean belongsToTimeRule(LocalTime tollCheckpoint, TimeRule timeRule) {
        return tollCheckpoint.isAfter(timeRule.getLeftLimit()) && tollCheckpoint.isBefore(timeRule.getRightLimit());
    }

    private boolean isTaxFreeVehicle(TaxConfiguration configuration, CongestionTaxRequest request) {
        return configuration.getTaxFreeVehicleTypes().contains(request.getVehicleType());
    }
}
