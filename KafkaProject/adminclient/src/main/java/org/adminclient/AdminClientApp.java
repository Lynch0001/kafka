package org.adminclient;

import java.util.concurrent.ExecutionException;

import org.adminclient.service.AdminService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminClientApp {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(AdminClientApp.class, args);


	}
}
