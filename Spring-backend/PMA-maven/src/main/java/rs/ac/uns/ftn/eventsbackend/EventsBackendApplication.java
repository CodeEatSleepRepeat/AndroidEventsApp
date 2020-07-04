package rs.ac.uns.ftn.eventsbackend;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

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
			
			String ip;
		    try {
		        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		        while (interfaces.hasMoreElements()) {
		            NetworkInterface iface = interfaces.nextElement();
		            // filters out 127.0.0.1 and inactive interfaces
		            if (iface.isLoopback() || !iface.isUp())
		                continue;

		            if (iface.getDisplayName().contains("Wireless-AC")) {
			            Enumeration<InetAddress> addresses = iface.getInetAddresses();
			            while(addresses.hasMoreElements()) {
			                InetAddress addr = addresses.nextElement();
			                ip = addr.getHostAddress();
			                System.out.println("\n\nIP Address: http://" + ip + ":" + port + "\n\n");
			    			return;
			            }
		            }
		        }
		    } catch (SocketException e) {
		        throw new RuntimeException(e);
		    }
		    
		    System.out.println("\n\nIP Address: http://" + inetAddress.getHostAddress() + ":" + port + "\n\n");
			
		} catch (UnknownHostException e) {}
	}
}
