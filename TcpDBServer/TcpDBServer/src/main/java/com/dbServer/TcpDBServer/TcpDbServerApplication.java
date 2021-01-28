package com.dbServer.TcpDBServer;

import TcpDBServerListener.TcpDBServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TcpDbServerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(TcpDbServerApplication.class);
		springApplication.addListeners(new TcpDBServer());
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);
	}

}
