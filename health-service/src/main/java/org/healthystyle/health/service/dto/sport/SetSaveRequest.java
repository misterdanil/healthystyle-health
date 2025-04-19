package org.healthystyle.health.service.dto.sport;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SetSaveRequest {
	@NotNull(message = "Укажите количество подходов")
	@Positive(message = "Количество подходов должно быть положительным числом")
	private Integer count;
	@NotNull(message = "Укажите количество повторений")
	@Positive(message = "Количество повторений должно быть положительным числом")
	private Integer repeat;
	@NotNull(message = "Укажите упражнение")
	private Long exerciseId;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	@Override
	public String toString() {
		return "SetSaveRequest [count=" + count + ", repeat=" + repeat + ", exerciseId=" + exerciseId + "]";
	}

}
