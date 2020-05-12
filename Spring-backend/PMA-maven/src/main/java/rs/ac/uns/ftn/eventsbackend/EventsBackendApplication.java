package rs.ac.uns.ftn.eventsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Boris
 *
 */
@SpringBootApplication
@EnableSwagger2
public class EventsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsBackendApplication.class, args);
	}

}
