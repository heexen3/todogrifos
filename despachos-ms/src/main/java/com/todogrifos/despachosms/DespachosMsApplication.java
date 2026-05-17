package com.todogrifos.despachosms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DespachosMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DespachosMsApplication.class, args);
	}

}