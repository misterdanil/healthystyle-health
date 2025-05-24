package org.healthystyle.health.service.notifier;

import org.healthystyle.health.repository.medicine.IntakeRepository.MissedDateIntake;

public interface IntakeNotifier {
	void notifyMissed(MissedDateIntake... intakes);
}
