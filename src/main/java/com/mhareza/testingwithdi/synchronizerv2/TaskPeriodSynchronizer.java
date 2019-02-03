package com.mhareza.testingwithdi.synchronizerv2;

public class TaskPeriodSynchronizer {

	private final Settings synchronizerSettings;

	public TaskPeriodSynchronizer(Settings synchronizerSettings) {
		this.synchronizerSettings = synchronizerSettings;
	}

	public void synchronize(Task task, Box box, boolean isExternalChange) {
		if (!synchronizerSettings.isTurnedOn()) {
			return;
		}
		if (isExternalChange && !synchronizerSettings.isTurnedOnForExternalChanges()) {
			return;
		}
		final Period newPeriod = calculateNewPeriod(task, box);
		boolean isPeriodValid = task.isPeriodValid(newPeriod);
		if (isPeriodValid) {
			task.updatePeriod(newPeriod);
		} else if (synchronizerSettings.isForce()) {
			task.setManualMode();
			task.updatePeriod(newPeriod);
		}
	}

	private Period calculateNewPeriod(Task task, Box box) {
		final TaskPeriodCalculator calculator = new TaskPeriodCalculator(synchronizerSettings.getStrategy());
		return calculator.calculateNewPeriod(task.getPeriod(), box.getPeriod());
	}
}
