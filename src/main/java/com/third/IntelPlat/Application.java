package com.third.IntelPlat;

/**
 * 
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wzm
 *
 */
@SpringBootApplication
@ComponentScan
@EnableScheduling
public class Application extends SpringBootServletInitializer
{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
	
	public static void main(String[] args) throws Exception{
		SpringApplication.run(Application.class, args);
	}
}
