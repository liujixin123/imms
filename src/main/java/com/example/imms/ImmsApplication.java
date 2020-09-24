package com.example.imms;

import com.example.imms.web.controller.CanalClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.imms.web.dao")
//public class ImmsApplication   implements CommandLineRunner{
public class ImmsApplication  {

//	@Autowired
//	CanalClient canalClient;

	public static void main(String[] args) {
		SpringApplication.run(ImmsApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("===============================同步台账数据 ====================================");
//		canalClient.run();
//	}


}
