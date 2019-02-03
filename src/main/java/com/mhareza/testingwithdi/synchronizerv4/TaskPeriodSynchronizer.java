package com.mhareza.testingwithdi.synchronizerv4;

public class TaskPeriodSynchronizer {

	private final Settings synchronizerSettings;

	private TaskPeriodCalculatorFactory calculatorFactory;

	public TaskPeriodSynchronizer(Settings synchronizerSettings, TaskPeriodCalculatorFactory calculatorFactory) {
		this.synchronizerSettings = synchronizerSettings;
		this.calculatorFactory = calculatorFactory;
	}

	public void synchronize(Task task, Box box, boolean isChangeExternal) {
		if (!synchronizerSettings.isTurnedOn()) {
			return;
		}
		if (isChangeExternal && !synchronizerSettings.isTurnedOnForExternalChanges()) {
			return;
		}
		final TaskPeriodCalculator calculator = calculatorFactory.getTaskPeriodCalculator(synchronizerSettings.getStrategy());
		final Period newPeriod = calculator.calculateNewPeriod(task.getPeriod(), box.getPeriod());
		boolean isPeriodValid = task.isPeriodValid(newPeriod);
		if (isPeriodValid) {
			task.updatePeriod(newPeriod);
		} else if (synchronizerSettings.isForce()) {
			task.setManualMode();
			task.updatePeriod(newPeriod);
		}
	}

}
