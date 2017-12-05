package it.italiancoders.jwt;

import it.italiancoders.jwt.dao.UserRepository;
import it.italiancoders.jwt.model.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@EnableJpaRepositories
@SpringBootApplication
public class JwtApplication {
	//private final String SAMPLE_DATA = "classpath:import.sql_";

	@Autowired
	private DataSource datasource;
	@Autowired
	private ApplicationContext webApplicationContext;

/*
	@PostConstruct
	public void loadIfInMemory() throws Exception {
		Resource resource = webApplicationContext.getResource(SAMPLE_DATA);
		ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
	}*/
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}
}
