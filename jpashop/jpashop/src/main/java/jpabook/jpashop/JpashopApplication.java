package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class JpashopApplication {


	@Transactional
	public static void main(String[] args) {
/*
		SpringApplication springApplication = new SpringApplication(JpashopApplication.class);
		//springApplication.addListeners(new TcpDBServer());
		springApplication.addListeners(new jpaHi());
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);*/

		SpringApplication.run(JpashopApplication.class, args);


	}

}
