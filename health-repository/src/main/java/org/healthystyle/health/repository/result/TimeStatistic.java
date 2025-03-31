package org.healthystyle.health.repository.result;

import java.time.LocalTime;

public class TimeStatistic extends Statistic {
	private LocalTime time;

	public TimeStatistic(LocalTime time, Float value) {
		super(value);
		this.time = time;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

}
