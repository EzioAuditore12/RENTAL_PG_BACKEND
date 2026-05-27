package com.rental_pg_backend.seeders;

import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.manager.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ManagerSeeder implements CommandLineRunner {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.seed.enabled.manager}")
    private boolean seedEnabled;

    @Override
    public void run(String... args) {

                System.out.println("ManagerSeeder: starting");

        if (!seedEnabled) {
                            System.out.println("ManagerSeeder: disabled by app.seed.enabled.manager=false");
            return;
        }

                long existingManagers = managerRepository.count();
                System.out.println("ManagerSeeder: existing manager count = " + existingManagers);

        List<String> firstNames = List.of(
                "Aarav", "Vivaan", "Aditya", "Vihaan",
                "Arjun", "Sai", "Krishna", "Kabir"
        );

        List<String> lastNames = List.of(
                "Sharma", "Verma", "Singh",
                "Patel", "Nair", "Reddy"
        );

        Random random = new Random();
        long seedSuffix = System.currentTimeMillis();
        long emailBase = existingManagers + 1;

        for (int i = 1; i <= 100; i++) {

            System.out.println("ManagerSeeder: creating manager " + i + "/100");

            String fullName =
                    firstNames.get(random.nextInt(firstNames.size()))
                            + " " +
                            lastNames.get(random.nextInt(lastNames.size()));

            Manager manager = new Manager();

            manager.setName(fullName);

            manager.setEmail(
                    "manager" + seedSuffix + "_" + (emailBase + i - 1) + "@example.com"
            );

            manager.setPassword(
                    passwordEncoder.encode("Example@123")
            );

            manager.setAvatar(
                    "https://api.dicebear.com/7.x/avataaars/png?seed="
                            + fullName.replace(" ", "")
            );

            managerRepository.save(manager);
        }

                System.out.println("ManagerSeeder: 100 managers appended successfully");
    }
}