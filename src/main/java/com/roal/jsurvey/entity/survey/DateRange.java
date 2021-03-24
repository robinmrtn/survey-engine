package com.roal.jsurvey.entity.survey;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class DateRange {

    private  LocalDate startDate;
    private  LocalDate endDate;

    public DateRange() {
    }

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date has to be before End Date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
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
