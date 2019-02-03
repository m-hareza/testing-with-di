package com.mhareza.testingwithdi.synchronizerv2;

public class Settings {


	private final boolean turnedOn;

	private final boolean force;

	private final boolean turnedOnForExternalChanges;

	private final TaskPeriodSynchronizerStrategy strategy;

	public Settings(
			boolean turnedOn,
			boolean force,
			boolean turnedOnForExternalChanges,
			TaskPeriodSynchronizerStrategy strategy) {
		this.turnedOn = turnedOn;
		this.force = force;
		this.turnedOnForExternalChanges = turnedOnForExternalChanges;
		this.strategy = strategy;
	}

	public boolean isTurnedOn() {
		return turnedOn;
	}

	public boolean isForce() {
		return force;
	}

	public boolean isTurnedOnForExternalChanges() {
		return turnedOnForExternalChanges;
	}

	public TaskPeriodSynchronizerStrategy getStrategy() {
		return strategy;
	}
}
