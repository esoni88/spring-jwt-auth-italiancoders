package it.italiancoders.jwt;

import it.italiancoders.jwt.dao.AuthorityRepository;
import it.italiancoders.jwt.dao.UserRepository;
import it.italiancoders.jwt.model.Authority;
import it.italiancoders.jwt.model.AuthorityName;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableJpaRepositories
@SpringBootApplication
public class JwtApplication {
	//private final String SAMPLE_DATA = "classpath:import.sql_";

	@Autowired
	private DataSource datasource;
	@Autowired
	private ApplicationContext webApplicationContext;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository, AuthorityRepository authorityRepository) {
		return (args) -> {

			User user=userRepository.findByUsername("admin");

			if(user == null){

				/**
				 * Inizializzo i dati del mio test
				 */


				Authority authorityAdmin=new Authority();
				authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
				authorityAdmin=authorityRepository.save(authorityAdmin);

				Authority authorityUser=new Authority();
				authorityUser.setName(AuthorityName.ROLE_USER);
				authorityUser=authorityRepository.save(authorityUser);


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityAdmin,authorityUser});


				user = new User();
				user.setAuthorities(authorities);
				user.setEmail("dario@prova.it");
				user.setEnabled(true);
				user.setFirstname("dario");
				user.setLastname("frongi");
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));

				user = userRepository.save(user);

			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}
}
