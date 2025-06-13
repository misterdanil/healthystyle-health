package org.healthystyle.health.repository.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.healthystyle.health.repository.timezone.TimeZoneContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Configuration
public class DataSourceConfig {

//	@Bean
//	@Primary
//	public DataSource dataSource() {
//		return ProxyDataSourceBuilder.create(d).beforeMethod((methodExecutionContext) -> {
//			Object target = methodExecutionContext.getTarget();
//			if (target instanceof Connection) {
//				Connection connection = (Connection) target;
//				String timezone = TimeZoneContext.getTimezone();
//				System.out.println("Prepare to change timezone to " + timezone);
//				if (timezone != null) {
//					try (Statement statement = connection.createStatement()) {
//						System.out.println("Changed timezone to " + timezone);
//						statement.execute("SET TIME ZONE '" + timezone + "'");
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).build();
//	}
	@Bean
	public BeanPostProcessor dataSourcePostProcessor() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof DataSource dataSource) {
					return ProxyDataSourceBuilder.create(dataSource).beforeMethod((methodExecutionContext) -> {
						Object target = methodExecutionContext.getTarget();
						if (target instanceof Connection) {
							Connection connection = (Connection) target;
							String timezone = TimeZoneContext.getTimezone();
							System.out.println("Prepare to change timezone to " + timezone);
							if (timezone != null) {
								try (Statement statement = connection.createStatement()) {
									System.out.println("Changed timezone to " + timezone);
									statement.execute("SET TIME ZONE '" + timezone + "'");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}
					}).build();
				}

				return bean;
			}

		};
	}
}
