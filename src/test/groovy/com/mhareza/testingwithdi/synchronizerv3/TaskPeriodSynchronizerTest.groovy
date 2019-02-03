package com.mhareza.testingwithdi.synchronizerv3

import spock.lang.Specification

import java.time.LocalDate

class TaskPeriodSynchronizerTest extends Specification {

	def "turned off synchronizer should not change...."() {
		given:
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = new Period(LocalDate.of(2010, 4, 20), LocalDate.of(2010, 04, 30))
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, new TaskPeriodCalculator(settings.strategy))

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer but turned off for external changes should not change...."() {
		given:
		Settings settings = new Settings(true, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = new Period(LocalDate.of(2010, 4, 20), LocalDate.of(2010, 04, 30))
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, new TaskPeriodCalculator(settings.strategy))

		when:
		synchronizer.synchronize(task, box, true)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer should not change period if date is invalid"() {
		given:
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = new Period(LocalDate.of(2010, 4, 20), LocalDate.of(2010, 04, 30))
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, false)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, new TaskPeriodCalculator(settings.strategy))

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer should change period if date is valid"() {
		given:
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.SMART)
		Task task = getTaskStub(TaskMode.AUTO, period("2017-10-20", "2017-10-23"), true)
		Period boxPeriod = period("2017-10-01", "2017-10-29")
		Box box = Stub(Box) {getPeriod() >> boxPeriod}
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, new TaskPeriodCalculator(settings.strategy))

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == period("2017-10-20", "2017-10-23")
		task.mode == TaskMode.AUTO
	}

	def period(String start, String end) {
		return new Period(
				LocalDate.parse(start),
				LocalDate.parse(end)
		)
	}

	Task getTaskStub(TaskMode mode, Period period, boolean periodValid) {
		return new Task(mode, period) {
			@Override
			boolean isPeriodValid(Period newPeriod) {
				return periodValid
			}
		}
	}

}
