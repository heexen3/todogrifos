package com.todogrifos.clientesms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientesMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientesMsApplication.class, args);
	}

}