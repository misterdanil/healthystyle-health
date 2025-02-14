package org.healthystyle.health.model.sport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import org.healthystyle.health.model.Health;

public class Sport {
	private Long id;
	private List<Schedule> schedules;
	private Instant start;
	private Instant end;
	private Health health;
	
	public static void main(String[] args) throws ParseException {
	System.out.println(new SimpleDateFormat("dd-MM-yyyy").parse("11-02-2025").toInstant().atZone(ZoneId.of("Europe/Moscow")));
	}
}
