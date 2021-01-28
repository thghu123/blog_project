package com.dbGather.TcpDBGather;

import TcpDBGatherListener.TcpDBGather;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TcpDbGatherApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(TcpDbGatherApplication.class);
		//springApplication.addListeners(new TcpDBGather());
		//스케줄러로 분리된다면 lister 사용X
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);

	}

}
