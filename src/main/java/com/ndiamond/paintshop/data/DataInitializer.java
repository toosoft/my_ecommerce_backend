package com.ndiamond.paintshop.data;

import com.ndiamond.paintshop.model.Role;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRole = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExists(defaultRole);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();

    }

    private void createDefaultUserIfNotExists() {
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");

        if (roleOptional.isEmpty()) {
            System.out.println("ROLE_USER not found");
            return;
        }

        Role userRole = roleOptional.get();


        for (int i = 1; i<=5; i++) {
            String defaultEmail =  "user"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" +i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user "+ i + "created successfully");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_ADMIN");

        if (roleOptional.isEmpty()) {
            System.out.println("ROLE_ADMIN not found");
            return;
        }

        Role adminRole = roleOptional.get();

        for (int i = 1; i<=2; i++) {
            String defaultEmail =  "admin"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" +i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user "+ i + "created successfully");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> role) {
        role.stream()
                .filter(roles -> roleRepository.findByName(roles).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
