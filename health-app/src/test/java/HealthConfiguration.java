import org.healthystyle.health.service.medicine.TreatmentService;
import org.healthystyle.health.service.medicine.impl.TreatmentServiceImpl;
import org.springframework.context.annotation.Bean;

//@org.springframework.boot.test.context.TestConfiguration
public class HealthConfiguration {
	
	@Bean
	public TreatmentService treatmentService() {
		return new TreatmentServiceImpl();
	}
}
