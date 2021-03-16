package com.roal.jsurvey.entity;

import java.time.LocalDate;

public class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    private DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date has to be before End Date");
        }

        return new DateRange(startDate, endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isBetween(LocalDate date) {
        return date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1));
    }
}
