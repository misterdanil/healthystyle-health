package org.healthystyle.health.service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	public static final String NOTIFICATION_DIRECT_EXCHANGE = "notification-direct-exchange";
	public static final String NOTIFICATION_ROUTING_KEY = "notification.save";

	public static final String IMAGE_CREATED_ROUTING_KEY = "article.image.created";
	public static final String IMAGE_CREATED_QUEUE = "article-image-created-queue";
	public static final String FRAGMENT_IMAGE_CREATED_ROUTING_KEY = "article.fragment.image.created";
	public static final String FRAGMENT_IMAGE_CREATED_QUEUE = "article-fragment-image-created-queue";

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}
}
