package org.healthystyle.health.model.sport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.Health;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = { @Index(name = "sport_start_idx", columnList = "start"),
		@Index(name = "sport_health_id_idx", columnList = "health_id") })
public class Sport {
	@Id
	@SequenceGenerator(name = "sport_generator", sequenceName = "sport_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "sport_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 1000)
	private String description;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sport")
	private List<Train> trains;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Instant start;
	@Temporal(TemporalType.DATE)
	private Instant end;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public Sport() {
		super();
	}

	public Sport(String description, Instant start, Health health, Train... trains) {
		super();

		Objects.requireNonNull(description, "Description must be not null");
		Objects.requireNonNull(start, "Start must be not null");
		Objects.requireNonNull(end, "End must be not null");
		Objects.requireNonNull(trains, "Trains must be not null");
		if (trains.length == 0) {
			throw new IllegalArgumentException("Must be passed at least one train");
		}

		this.description = description;
		this.trains = new ArrayList<>(Arrays.asList(trains));
		this.start = start;
		this.health = health;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Train> getTrains() {
		if (trains == null) {
			trains = new ArrayList<>();
		}
		return trains;
	}

	public void addTrains(Train... trains) {
		getTrains().addAll(Arrays.asList(trains));
	}

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}

	public Health getHealth() {
		return health;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(
				new SimpleDateFormat("dd-MM-yyyy").parse("11-02-2025").toInstant().atZone(ZoneId.of("Europe/Moscow")));
	}
}
