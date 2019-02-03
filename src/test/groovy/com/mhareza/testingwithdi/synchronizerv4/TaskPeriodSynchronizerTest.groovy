package com.mhareza.testingwithdi.synchronizerv4

import spock.lang.Specification

import java.time.LocalDate

class TaskPeriodSynchronizerTest extends Specification {

	TaskPeriodCalculatorFactory calculatorFactory

	def setup() {
		calculatorFactory = Stub(TaskPeriodCalculatorFactory)
	}


	def "turned off synchronizer should leave task unchanged"() {
		given: "turned off synchronizer"
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)
		and: "task and box"
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)

		when: "trying to synchronize internal change"
		synchronizer.synchronize(task, box, false)

		then: "period and mode remain the same "
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on but only for internal changes synchronizer should leave task unchanged if change is external"() {
		given: "turned on only for internal changes synchronizer"
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)
		and: "task and box"
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)

		when: "trying to synchronize external change"
		synchronizer.synchronize(task, box, false)

		then: "period and mode remain the same "
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on for all changes but not force synchronizer should leave task unchanged if period is ivnalid"() {
		given: "turned on only for internal changes synchronizer"
		Settings settings = new Settings(false, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)
		and: "task (with period never valid) and box"
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)

		when: "trying to synchronize internal change change"
		synchronizer.synchronize(task, box, false)

		then: "period and mode remain the same"
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer should not change if external change...."() {
		given:
		Settings settings = new Settings(true, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, true)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)

		when:
		synchronizer.synchronize(task, box, true)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on synchronizer should not change if not force and period is invalid...."() {
		given:
		Settings settings = new Settings(true, false, false, TaskPeriodSynchronizerStrategy.PRECISE)
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, false)
		Box box = Stub(Box)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)

		when:
		synchronizer.synchronize(task, box, true)

		then:
		task.period == taskPeriod
		task.mode == TaskMode.AUTO
	}

	def "turned on force synchronizer should change date if is invalid and change its mode to auto"() {
		given: "force turned on synchronizer"
		Settings settings = new Settings(true, true, false, TaskPeriodSynchronizerStrategy.PRECISE)
		TaskPeriodSynchronizer synchronizer = new TaskPeriodSynchronizer(settings, calculatorFactory)
		and: "task (with period never valid) and box"
		Period taskPeriod = Stub(Period)
		Task task = getTaskStub(TaskMode.AUTO, taskPeriod, false)
		Box box = Stub(Box)
		and: "calculator returning expected period"
		Period expectedPeriod = Stub(Period)
		setPeriodReturnedByCalculator(TaskPeriodSynchronizerStrategy.PRECISE, expectedPeriod)

		when:
		synchronizer.synchronize(task, box, false)

		then:
		task.period == expectedPeriod
		task.mode == TaskMode.MANUAL
	}

	void setPeriodReturnedByCalculator(TaskPeriodSynchronizerStrategy strategy, Period adjustedPeriod) {
		calculatorFactory.getTaskPeriodCalculator(strategy) >> Stub(TaskPeriodCalculator) { calculateNewPeriod(_, _) >> adjustedPeriod }
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
