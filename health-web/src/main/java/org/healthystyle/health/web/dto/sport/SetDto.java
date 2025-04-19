package org.healthystyle.health.web.dto.sport;

public class SetDto {
	private Long id;
	private Integer count;
	private Integer repeat;
	private ExerciseDto exercise;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public ExerciseDto getExercise() {
		return exercise;
	}

	public void setExercise(ExerciseDto exercise) {
		this.exercise = exercise;
	}

}
