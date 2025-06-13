package org.healthystyle.health.service.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.healthystyle.health.repository.diet.MealRepository;
import org.healthystyle.health.repository.diet.dto.MissedMeal;
import org.healthystyle.health.service.notifier.MealNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MealScheduler {
	@Autowired
	private MealNotifier notifier;
	@Autowired
	private MealRepository repository;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void reportMeals() {
		System.out.println("report " + Instant.now());
		Instant start = Instant.now();
		Instant end = start.plus(1, ChronoUnit.HOURS);
		List<MissedMeal> meals = repository.findNotExecuted(start, end);
		notifier.notifyMissed(meals.toArray(new MissedMeal[meals.size()]));
	}
}
