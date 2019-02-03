package com.mhareza.testingwithdi.synchronizerv1

import spock.lang.Specification

import java.time.LocalDate

class TaskPeriodSynchronizerTest extends Specification {


	def "turned off synchronizer should not change...."() {
		given:
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = period("2010-04-20", "2010-04-30")
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings)

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer but turned off for external changes should not change...."() {
		given:
		Settings settings = new Settings(true, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = period("2010-04-20", "2010-04-30")
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings)

		when:
		synchronizer.synchronize(task, box, true)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer should not change period if date is invalid"() {
		given:
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = period("2010-04-20", "2010-04-30")
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, false)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings)

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on scynchronizer with smart alignment should change period if date is valid"(Period taskPeriod, Period boxPeriod, Period expectedPeriod) {
		given:
		Settings settings = new Settings(true, false, false, TaskPeriodSynchronizerStrategy.SMART)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box) { getPeriod() >> boxPeriod }
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings)

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == expectedPeriod
		task.mode == TaskMode.AUTO

		where:
		taskPeriod                         | boxPeriod                          | expectedPeriod
		period("2017-10-20", "2017-10-23") | period("2017-10-01", "2017-10-29") | period("2017-10-20", "2017-10-23")
		period("2017-10-20", "2017-10-23") | period("2017-10-01", "2017-10-09") | period("2017-10-06", "2017-10-09")
		period("2017-10-20", "2017-10-29") | period("2017-11-01", "2017-11-04") | period("2017-11-01", "2017-11-04")
		period("2017-10-20", "2017-10-23") | period("2017-10-22", "2017-10-28") | period("2017-10-22", "2017-10-25")
		period("2017-10-20", "2017-10-23") | period("2017-10-10", "2017-10-30") | period("2017-10-20", "2017-10-23")
	}

	def "turned on ( .... )period if date is not valid but is force"(Period taskPeriod, Period boxPeriod, Period expectedPeriod) {
		given:
		Settings settings = new Settings(true, true, false, TaskPeriodSynchronizerStrategy.SMART)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, false)
		Box box = Stub(Box) { getPeriod() >> boxPeriod }
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings)

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == expectedPeriod
		task.mode == TaskMode.MANUAL

		where:
		taskPeriod                         | boxPeriod                          | expectedPeriod
		period("2017-10-20", "2017-10-23") | period("2017-10-01", "2017-10-29") | period("2017-10-20", "2017-10-23")
		period("2017-10-20", "2017-10-23") | period("2017-10-01", "2017-10-09") | period("2017-10-06", "2017-10-09")
		period("2017-10-20", "2017-10-29") | period("2017-11-01", "2017-11-04") | period("2017-11-01", "2017-11-04")
		period("2017-10-20", "2017-10-23") | period("2017-10-22", "2017-10-28") | period("2017-10-22", "2017-10-25")
		period("2017-10-20", "2017-10-23") | period("2017-10-10", "2017-10-30") | period("2017-10-20", "2017-10-23")
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
