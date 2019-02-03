package com.mhareza.testingwithdi.synchronizerv4;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SmartTaskPeriodCalculator implements TaskPeriodCalculator {

	public Period calculateNewPeriod(Period periodToBeAdjusted, Period modelPeriod) {
		boolean periodStartIsInsideBox = modelPeriod.contains(periodToBeAdjusted.getStartDate());
		boolean periodEndIsInsideBox = modelPeriod.contains(periodToBeAdjusted.getEndDate());

		if (periodStartIsInsideBox && periodEndIsInsideBox) {
			return periodToBeAdjusted;
		}

		final long periodLengthInDays = periodToBeAdjusted.getDurationInDays();
		final long boxLengthInDays = modelPeriod.getDurationInDays();

		if (boxLengthInDays < periodLengthInDays) {
			return new Period(modelPeriod);
		}

		final LocalDate boxStartDate = modelPeriod.getStartDate();
		final LocalDate boxEndDate = modelPeriod.getEndDate();
		if (periodToBeAdjusted.getStartDate().isBefore(boxStartDate)) {
			return new Period(boxStartDate, boxStartDate.plus(periodLengthInDays, ChronoUnit.DAYS));
		}

		if (periodToBeAdjusted.getEndDate().isAfter(boxEndDate)) {
			return new Period(boxEndDate.minus(periodLengthInDays, ChronoUnit.DAYS), boxEndDate);
		}

		if (periodStartIsInsideBox) {
			final LocalDate endDate = periodToBeAdjusted.getStartDate().plus(periodLengthInDays, ChronoUnit.DAYS);
			if (endDate.isAfter(boxEndDate)) {
				return new Period(boxEndDate.minus(periodLengthInDays, ChronoUnit.DAYS), boxEndDate);
			}
			return new Period(periodToBeAdjusted.getStartDate(), endDate);
		}

		if (periodEndIsInsideBox) {
			final LocalDate startDate = periodToBeAdjusted.getEndDate().minus(periodLengthInDays, ChronoUnit.DAYS);
			if (startDate.isBefore(boxStartDate)) {
				return new Period(boxStartDate, boxStartDate.plus(periodLengthInDays, ChronoUnit.DAYS));
			}
			return new Period(startDate, periodToBeAdjusted.getEndDate());
		}

		throw new IllegalArgumentException();
	}

}
