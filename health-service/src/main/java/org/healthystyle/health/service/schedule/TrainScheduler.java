package org.healthystyle.health.service.schedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.healthystyle.health.repository.sport.TrainRepository;
import org.healthystyle.health.repository.sport.TrainRepository.MissedTrain;
import org.healthystyle.health.service.notifier.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrainScheduler {
	@Autowired
	private Notifier<MissedTrain> notifier;
	@Autowired
	private TrainRepository repository;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void reportTrains() {
		System.out.println("report " + Instant.now());
		Instant start = Instant.now();
		Instant end = start.plus(1, ChronoUnit.HOURS);
		List<MissedTrain> intakes = repository.findNotExecuted(start, end);
		notifier.notify(intakes);
	}
}
