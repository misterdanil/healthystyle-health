package org.healthystyle.health.service.notifier.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.healthystyle.health.repository.medicine.IntakeRepository.MissedDateIntake;
import org.healthystyle.health.service.config.RabbitConfig;
import org.healthystyle.health.service.notifier.IntakeNotifier;
import org.healthystyle.health.service.notifier.NotificationSaveRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntakeNotifierImpl implements IntakeNotifier {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void notifyMissed(MissedDateIntake... intakes) {
		for (MissedDateIntake intake : intakes) {
			NotificationSaveRequest notification = new NotificationSaveRequest();
			notification.setTitle("Не забудьте принять лекарство " + intake.getMedicineName() + ", назначенное на "
					+ intake.getDate());
			notification.setIdentifier("intake " + intake.getId() + " "
					+ DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(new Locale("ru", "RU"))
							.withZone(ZoneId.systemDefault()).format(intake.getDate()));
			notification.setToUserId(intake.getUserId());
			notification.setType("medicine");
			rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_DIRECT_EXCHANGE,
					RabbitConfig.NOTIFICATION_ROUTING_KEY, notification);
		}
	}

	public static void main(String[] args) {
		System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(new Locale("ru", "RU"))
				.withZone(ZoneId.systemDefault()).format(Instant.now()));
	}

}
