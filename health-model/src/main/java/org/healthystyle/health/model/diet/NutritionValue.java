package org.healthystyle.health.model.diet;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "nutrition_value")
public class NutritionValue {
	@Id
	@SequenceGenerator(name = "nutrition_value_generator", sequenceName = "nutrition_value_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "nutrition_value_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Value value;
	@ManyToOne
	@JoinColumn(name = "measure_id", nullable = false)
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "convert_type_id", nullable = false)
	private ConvertType convertType;
	
	

}
