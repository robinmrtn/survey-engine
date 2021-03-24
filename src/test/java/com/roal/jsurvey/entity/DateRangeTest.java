package com.roal.jsurvey.entity;


import com.roal.jsurvey.entity.survey.DateRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DateRangeTest {
    @Test
    @DisplayName("startDateIsAfterEndDate - Exception")
    public void startDateIsAfterEndDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DateRange dateRange = new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2021, 2, 2));
        });
    }

    @Test
    @DisplayName("dateIsInRange")
    public void dateIsInRange() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 2, 2));

        LocalDate dateEqualsStartDate = LocalDate.of(2021, 1, 1);
        LocalDate dateInRange = LocalDate.of(2021, 6, 1);
        LocalDate dateEqualsEndDate = LocalDate.of(2022, 2, 2);

        Assertions.assertTrue(dateRange.isBetween(dateEqualsStartDate));
        Assertions.assertTrue(dateRange.isBetween(dateInRange));
        Assertions.assertTrue(dateRange.isBetween(dateEqualsEndDate));

    }

    @Test
    @DisplayName("dateIsNotInRange")
    public void dateIsNotInRange() {
        DateRange dateRange = new DateRange(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 2, 2));

        LocalDate dateBeforeRange = LocalDate.of(2020, 12, 31);
        LocalDate dateAfterRange = LocalDate.of(2022, 2, 3);

        Assertions.assertFalse(dateRange.isBetween(dateBeforeRange));
        Assertions.assertFalse(dateRange.isBetween(dateAfterRange));


    }
}
