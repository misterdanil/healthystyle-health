package org.healthystyle.health.repository.result;

import java.time.LocalDate;

public class DateStatistic extends Statistic {
	private LocalDate date;

	public DateStatistic(LocalDate date, Float value) {
		super(value);
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
