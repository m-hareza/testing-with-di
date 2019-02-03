package com.mhareza.testingwithdi.synchronizerv4;

public interface TaskPeriodCalculator {

	Period calculateNewPeriod(Period periodToBeAdjusted, Period modelPeriod);

}
