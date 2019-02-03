package com.mhareza.testingwithdi.synchronizerv4;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PreciseTaskPeriodCalculator implements TaskPeriodCalculator{

	@Override
	public Period calculateNewPeriod(Period periodToBeAdjusted, Period modelPeriod) {
		return modelPeriod;
	}

}
