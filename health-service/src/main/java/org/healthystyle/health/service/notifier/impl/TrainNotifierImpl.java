package org.healthystyle.health.service.notifier.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

import org.healthystyle.health.model.Timezone;
import org.healthystyle.health.repository.sport.TrainRepository.MissedTrain;
import org.healthystyle.health.service.config.RabbitConfig;
import org.healthystyle.health.service.notifier.NotificationSaveRequest;
import org.healthystyle.health.service.notifier.Notifier;
import org.healthystyle.health.service.timezone.TimeZoneService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainNotifierImpl implements Notifier<MissedTrain> {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private TimeZoneService timeZoneService;

	@Override
	public void notify(List<MissedTrain> trains) {
		for (MissedTrain train : trains) {
			NotificationSaveRequest notification = new NotificationSaveRequest();

			Timezone timezone = timeZoneService.findByUserId(train.getUserId());

			String format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(new Locale("ru", "RU"))
					.withZone(ZoneId.of(timezone.getTimezone())).format(train.getDate());

			notification.setTitle("Занятие по упражнению '" + train.getExercise() + "', состоится: " + train.getDate());
			notification.setIdentifier("set " + train.getId() + " " + format);
			notification.setToUserId(train.getUserId());
			notification.setType("medicine");

			rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_DIRECT_EXCHANGE,
					RabbitConfig.NOTIFICATION_ROUTING_KEY, notification);
		}
	}

}
