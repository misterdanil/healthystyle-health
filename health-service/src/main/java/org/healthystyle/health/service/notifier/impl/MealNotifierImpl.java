package org.healthystyle.health.service.notifier.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.healthystyle.health.repository.diet.dto.MissedMeal;
import org.healthystyle.health.service.config.RabbitConfig;
import org.healthystyle.health.service.notifier.MealNotifier;
import org.healthystyle.health.service.notifier.NotificationSaveRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MealNotifierImpl implements MealNotifier {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void notifyMissed(MissedMeal... meals) {
		for (MissedMeal meal : meals) {
			NotificationSaveRequest notification = new NotificationSaveRequest();
			String format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(new Locale("ru", "RU"))
					.withZone(ZoneId.systemDefault()).format(meal.getDate());
			notification.setTitle("У вас приём пищи '" + meal.getFoodName() + "':" + format);
			notification.setIdentifier("meal " + meal.getId() + " " + format);
			notification.setToUserId(meal.getUserId());
			notification.setType("meal");
			rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_DIRECT_EXCHANGE,
					RabbitConfig.NOTIFICATION_ROUTING_KEY, notification);
		}

	}

}
