package com.todogrifos.devolucionesms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Habilita la intercomunicación con ventas-ms e inventario-ms
public class DevolucionesMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevolucionesMsApplication.class, args);
	}

}