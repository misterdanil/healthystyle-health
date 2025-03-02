package org.healthystyle.health.repository.result;

import java.time.Instant;

public class AvgStatistic {
	private Instant date;
	private String average;

	public AvgStatistic(Instant date, String average) {
		super();
		this.date = date;
		this.average = average;
	}

	public Instant getDate() {
		return date;
	}

	public String getAverage() {
		return average;
	}

}
