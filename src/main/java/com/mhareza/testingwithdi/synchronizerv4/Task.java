package com.mhareza.testingwithdi.synchronizerv4;

public abstract class Task {

	private TaskMode mode;

	private Period period;

	public Task(TaskMode mode, Period period) {
		this.mode = mode;
		this.period = period;
	}

	void updatePeriod(Period period) {
		assert mode != TaskMode.AUTO || isPeriodValid(period);
		this.period = period;
	}

	abstract boolean isPeriodValid(Period period);


	void setManualMode() {
		this.mode = TaskMode.MANUAL;
	}

	Period getPeriod() {
		return period;
	}

	public TaskMode getMode() {
		return mode;
	}
}
