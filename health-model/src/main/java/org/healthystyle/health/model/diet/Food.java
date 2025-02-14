package org.healthystyle.health.model.diet;

import java.util.List;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

public class Food {
	private Long id;
	private String name;
	private String weight;
	private List<FoodValue> foodValue;
	private Measure measure;
	private ConvertType convertType;
	
}
