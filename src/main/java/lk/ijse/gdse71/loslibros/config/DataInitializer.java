package lk.ijse.gdse71.loslibros.config;

import lk.ijse.gdse71.loslibros.entity.Role;
import lk.ijse.gdse71.loslibros.entity.User;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .address("address")
                    .email("admin@loslibros.com")
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(adminUser);
            System.out.println("Admin user created successfully");
        } else {
            System.out.println("Admin user already exists");
        }
    }
}