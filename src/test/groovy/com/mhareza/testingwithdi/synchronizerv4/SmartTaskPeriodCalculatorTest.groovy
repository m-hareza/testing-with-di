package com.mhareza.testingwithdi.synchronizerv4

import spock.lang.Specification

import java.time.LocalDate

class SmartTaskPeriodCalculatorTest extends Specification {

	def "smart should calculate correct date"(Period taskPeriod, Period boxPeriod, Period expectedPeriod) {
		given:
		TaskPeriodCalculator calculator = new SmartTaskPeriodCalculator()

		expect:
		calculator.calculateNewPeriod(taskPeriod, boxPeriod) == expectedPeriod

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

}
