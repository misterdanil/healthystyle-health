package org.healthystyle.health.service.notifier;

import java.util.List;

public interface Notifier<T> {
	void notify(List<T> objects);

}
