package ma.enset.conferenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConferenceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceServiceApplication.class, args);
	}

}
