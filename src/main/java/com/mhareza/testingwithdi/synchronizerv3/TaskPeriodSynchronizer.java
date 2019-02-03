package com.mhareza.testingwithdi.synchronizerv3;

public class TaskPeriodSynchronizer {

	private final Settings synchronizerSettings;

	private final TaskPeriodCalculator periodCalculator;

	public TaskPeriodSynchronizer(Settings synchronizerSettings, TaskPeriodCalculator periodCalculator) {
		this.synchronizerSettings = synchronizerSettings;
		this.periodCalculator = periodCalculator;
	}

	public void synchronize(Task task, Box box, boolean isExternalChange) {
		if (!synchronizerSettings.isTurnedOn()) {
			return;
		}
		if (isExternalChange && !synchronizerSettings.isTurnedOnForExternalChanges()) {
			return;
		}
		final Period newPeriod = periodCalculator.calculateNewPeriod(task.getPeriod(), box.getPeriod());
		boolean isPeriodValid = task.isPeriodValid(newPeriod);
		if (isPeriodValid) {
			task.updatePeriod(newPeriod);
		} else if (synchronizerSettings.isForce()) {
			task.setManualMode();
			task.updatePeriod(newPeriod);
		}
	}

}
