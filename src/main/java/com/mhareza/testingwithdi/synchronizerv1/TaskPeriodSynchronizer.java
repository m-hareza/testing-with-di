package com.mhareza.testingwithdi.synchronizerv1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
		Period taskPeriod = task.getPeriod();
		Period boxPeriod = box.getPeriod();
		if (TaskPeriodSynchronizerStrategy.PRECISE == synchronizerSettings.getStrategy()) {
			return boxPeriod;
		}
		
		boolean periodStartIsInsideBox = boxPeriod.contains(taskPeriod.getStartDate());
		boolean periodEndIsInsideBox = boxPeriod.contains(taskPeriod.getEndDate());

		if (periodStartIsInsideBox && periodEndIsInsideBox) {
			return taskPeriod;
		}

		final long periodLengthInDays = taskPeriod.getDurationInDays();
		final long boxLengthInDays = boxPeriod.getDurationInDays();

		if (boxLengthInDays < periodLengthInDays) {
			return new Period(boxPeriod);
		}

		final LocalDate boxStartDate = boxPeriod.getStartDate();
		final LocalDate boxEndDate = boxPeriod.getEndDate();
		if (taskPeriod.getStartDate().isBefore(boxStartDate)) {
			return new Period(boxStartDate, boxStartDate.plus(periodLengthInDays, ChronoUnit.DAYS));
		}

		if (taskPeriod.getEndDate().isAfter(boxEndDate)) {
			return new Period(boxEndDate.minus(periodLengthInDays, ChronoUnit.DAYS), boxEndDate);
		}

		if (periodStartIsInsideBox) {
			final LocalDate endDate = taskPeriod.getStartDate().plus(periodLengthInDays, ChronoUnit.DAYS);
			if (endDate.isAfter(boxEndDate)) {
				return new Period(boxEndDate.minus(periodLengthInDays, ChronoUnit.DAYS), boxEndDate);
			}
			return new Period(taskPeriod.getStartDate(), endDate);
		}

		if (periodEndIsInsideBox) {
			final LocalDate startDate = taskPeriod.getEndDate().minus(periodLengthInDays, ChronoUnit.DAYS);
			if (startDate.isBefore(boxStartDate)) {
				return new Period(boxStartDate, boxStartDate.plus(periodLengthInDays, ChronoUnit.DAYS));
			}
			return new Period(startDate, taskPeriod.getEndDate());
		}

		throw new IllegalArgumentException();
	}
}
