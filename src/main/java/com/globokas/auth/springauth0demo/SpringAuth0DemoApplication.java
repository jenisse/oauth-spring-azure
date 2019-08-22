package com.globokas.auth.springauth0demo;

import com.globokas.auth.springauth0demo.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SpringAuth0DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAuth0DemoApplication.class, args);
	}

}
