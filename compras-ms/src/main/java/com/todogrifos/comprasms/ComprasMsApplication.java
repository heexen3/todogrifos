package com.todogrifos.comprasms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Requisito Rúbrica: Activa el escaneo para intercomunicación remota
public class ComprasMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComprasMsApplication.class, args);
	}

}