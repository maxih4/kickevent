package com.example.kickevent;

import com.example.kickevent.model.Role;
import com.example.kickevent.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KickeventApplication {

	public static void main(String[] args) {
		SpringApplication.run(KickeventApplication.class, args);
	}

	@Bean
	CommandLineRunner initializeRoles(RoleRepository roleRepository) {
		return args -> {
			createRoleIfMissing(roleRepository, "USER");
			createRoleIfMissing(roleRepository, "ADMIN");
		};
	}

	private void createRoleIfMissing(RoleRepository roleRepository, String roleName) {
		if (roleRepository.findByName(roleName).isEmpty()) {
			Role role = new Role();
			role.setName(roleName);
			roleRepository.save(role);
		}
	}
}
