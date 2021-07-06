package com.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableEurekaClient
public class TimesheetmicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetmicroservicesApplication.class, args);
	}

//	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
