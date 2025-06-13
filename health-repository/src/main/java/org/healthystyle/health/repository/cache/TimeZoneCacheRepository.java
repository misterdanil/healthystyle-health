package org.healthystyle.health.repository.cache;

import org.healthystyle.health.repository.cache.dto.CachedTimeZone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneCacheRepository extends CrudRepository<CachedTimeZone, String>{
	CachedTimeZone findByUserId(String userId);
}
