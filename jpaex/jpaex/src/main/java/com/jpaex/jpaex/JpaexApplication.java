package com.jpaex.jpaex;

import entity.SysStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import repository.SysStatRepository;

@SpringBootApplication
public class JpaexApplication {

	private static SysStatRepository sysstatRepository;

	@Autowired
	public JpaexApplication(SysStatRepository sysstatRepository) {
		this.sysstatRepository = sysstatRepository;
	}

	public static void main(String[] args) {
		SysStat sysstat1 = new SysStat("test1");
		SysStat sysstat2 = new SysStat("test2");

		//시간만 저장하는 방법
		sysstatRepository.save(sysstat1);
		sysstatRepository.save(sysstat2);

		SpringApplication springApplication = new SpringApplication(JpaexApplication.class);
		//springApplication.addListeners(new jpaListner());
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);

	}

}
