package org.healthystyle.health.service.notifier;

import org.healthystyle.health.repository.diet.dto.MissedMeal;

public interface MealNotifier {
	void notifyMissed(MissedMeal... meals);

}
