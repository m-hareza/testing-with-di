package com.mhareza.testingwithdi.synchronizerv4;

public class TaskPeriodCalculatorFactory {

	TaskPeriodCalculator getTaskPeriodCalculator(TaskPeriodSynchronizerStrategy strategy) {
		if (strategy == TaskPeriodSynchronizerStrategy.PRECISE) {
			return new PreciseTaskPeriodCalculator();
		} else if (strategy == TaskPeriodSynchronizerStrategy.SMART) {
			return new SmartTaskPeriodCalculator();
		}
		throw new IllegalArgumentException();
	}

}
