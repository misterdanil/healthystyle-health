package org.healthystyle.health.repository.diet.dto;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.healthystyle.health.model.measure.Type;

public class MissedMeal {
	private Long id;
	private LocalDate start;
	private LocalDate end;
	private LocalTime time;
	private Integer day;
	private Float weight;
	private String measure;
	private String foodName;
	private LocalDateTime date;
	private Long userId;

	public MissedMeal(Long id, Date start, Date end, Time time, 
			Short day, Float weight, String measure,
			String foodName, Object date, Long userId) {
		super();
		this.id = id;
		this.start = Instant.ofEpochMilli(start.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		this.end = Instant.ofEpochMilli(end.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		this.time = Instant.ofEpochMilli(time.getTime()).atZone(ZoneId.systemDefault()).toLocalTime();
		this.day = day.intValue();
		this.weight = weight.floatValue();
		this.measure = Type.valueOf(measure).toString();
		this.foodName = foodName;
		this.date = ((Timestamp) date).toLocalDateTime();
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
