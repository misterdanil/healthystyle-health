package org.healthystyle.health.repository.cache.dto;

import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class CachedTimeZone {
	private String id;
	private String userId;
	private String timezone;

	public CachedTimeZone(String id, String userId, String timezone) {
		super();
		this.id = id;
		this.userId = userId;
		this.timezone = timezone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
