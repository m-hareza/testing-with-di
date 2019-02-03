package com.mhareza.testingwithdi.synchronizerv1;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Period {

	private final LocalDate startDate;

	private final LocalDate endDate;

	public Period(Period boxPeriod) {
		this(boxPeriod.startDate, boxPeriod.endDate);
	}

	public Period(LocalDate startDate, LocalDate endDate) {

		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean contains(LocalDate date) {
		return !date.isBefore(startDate) && !date.isAfter(endDate);
	}

	public Long getDurationInDays() {
		return ChronoUnit.DAYS.between(startDate, endDate);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Period period = (Period) o;

		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(startDate, period.startDate)
				.append(endDate, period.endDate)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
				.append(startDate)
				.append(endDate)
				.toHashCode();
	}
}
