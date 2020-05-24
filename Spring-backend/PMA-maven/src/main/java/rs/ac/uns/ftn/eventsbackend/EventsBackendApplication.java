package rs.ac.uns.ftn.eventsbackend;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
		ConfigurableApplicationContext c = SpringApplication.run(EventsBackendApplication.class, args);
		
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String port = c.getEnvironment().getProperty("server.port");
			System.out.println("\n\nIP Address: http://" + inetAddress.getHostAddress() + ":" + port + "\n\n");
		} catch (UnknownHostException e) {}
	}
}
