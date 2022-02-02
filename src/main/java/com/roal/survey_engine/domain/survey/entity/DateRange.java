package com.roal.survey_engine.domain.survey.entity;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
public class DateRange {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRange() {
        // needed by hibernate
    }

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date has to be before End Date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isBetween(LocalDate date) {
        return date.isAfter(startDate.toLocalDate().minusDays(1)) && date.isBefore(endDate.toLocalDate().plusDays(1));
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateRange dateRange = (DateRange) o;

        if (!startDate.equals(dateRange.startDate)) return false;
        return endDate.equals(dateRange.endDate);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }
}
