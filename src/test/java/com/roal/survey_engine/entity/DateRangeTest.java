package com.roal.survey_engine.entity;


import com.roal.survey_engine.domain.survey.entity.DateRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {
    @Test
    @DisplayName("start date is after end date throws Exception")
    void startDateIsAfterEndDate() {

        var startDate = LocalDate.of(2022, 1, 1);
        var endDate = LocalDate.of(2021, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            DateRange dateRange = new DateRange(startDate, endDate);
        });
    }

    @Test
    @DisplayName("date is in range")
    void dateIsInRange() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 2, 2));

        LocalDate dateEqualsStartDate = LocalDate.of(2021, 1, 1);
        LocalDate dateInRange = LocalDate.of(2021, 6, 1);
        LocalDate dateEqualsEndDate = LocalDate.of(2022, 2, 2);

        assertTrue(dateRange.isBetween(dateEqualsStartDate));
        assertTrue(dateRange.isBetween(dateInRange));
        assertTrue(dateRange.isBetween(dateEqualsEndDate));
    }

    @Test
    @DisplayName("date is not in range")
    void dateIsNotInRange() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 2, 2));

        LocalDate dateBeforeRange = LocalDate.of(2020, 12, 31);
        LocalDate dateAfterRange = LocalDate.of(2022, 2, 3);

        assertFalse(dateRange.isBetween(dateBeforeRange));
        assertFalse(dateRange.isBetween(dateAfterRange));
    }

    @Test
    @DisplayName("two identical ranges are equal")
    void rangesAreEqual() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 2, 2));

        DateRange dateRange2 = new DateRange(LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 2, 2));

        assertTrue(dateRange.equals(dateRange2));
    }

    @Test
    @DisplayName("two different ranges are not equal")
    void rangesAreNotEqual() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 2, 2));

        DateRange dateRange2 = new DateRange(LocalDate.of(2021, 1, 2),
                LocalDate.of(2022, 2, 2));

        assertFalse(dateRange.equals(dateRange2));
    }
}
