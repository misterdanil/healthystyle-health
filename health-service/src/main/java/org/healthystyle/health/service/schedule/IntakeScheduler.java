package org.healthystyle.health.service.schedule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.healthystyle.health.repository.medicine.IntakeRepository;
import org.healthystyle.health.repository.medicine.IntakeRepository.MissedDateIntake;
import org.healthystyle.health.service.notifier.IntakeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IntakeScheduler {
	@Autowired
	private IntakeNotifier notifier;
	@Autowired
	private IntakeRepository repository;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void reportNotExecutedIntakes() {
		System.out.println("report " + Instant.now());
		Instant start = Instant.now();
		Instant end = start.plus(1, ChronoUnit.HOURS);
		List<MissedDateIntake> intakes = repository.findNotExecuted(start, end);
		notifier.notifyMissed(intakes.toArray(new MissedDateIntake[intakes.size()]));
	}
}
