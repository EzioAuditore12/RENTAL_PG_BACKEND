package com.rental_pg_backend.seeders;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rental_pg_backend.tenant.entities.Tenant;
import com.rental_pg_backend.tenant.repositories.TenantRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantSeeder implements CommandLineRunner {

	private final TenantRepository tenantRepository;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Value("${app.seed.enabled.tenant}")
	private boolean seedEnabled;

	@Override
	public void run(String... args) {

		System.out.println("TenantSeeder: starting");

		if (!seedEnabled) {
			System.out.println("TenantSeeder: disabled by app.seed.enabled.tenant=false");
			return;
		}

		long existingTenants = tenantRepository.count();
		System.out.println("TenantSeeder: existing tenant count = " + existingTenants);

		List<String> firstNames = List.of(
				"Aarav", "Vivaan", "Aditya", "Vihaan",
				"Arjun", "Sai", "Krishna", "Kabir",
				"Aditi", "Ananya", "Diya", "Ira",
				"Meera", "Kavya", "Saanvi", "Priya"
		);

		List<String> lastNames = List.of(
				"Sharma", "Verma", "Singh",
				"Patel", "Nair", "Reddy",
				"Gupta", "Joshi", "Iyer", "Khan"
		);

		Random random = new Random();
		long seedSuffix = System.currentTimeMillis();
		long emailBase = existingTenants + 1;

		for (int i = 1; i <= 100; i++) {
			System.out.println("TenantSeeder: creating tenant " + i + "/100");

			String fullName =
					firstNames.get(random.nextInt(firstNames.size()))
							+ " "
							+ lastNames.get(random.nextInt(lastNames.size()));

			Tenant tenant = new Tenant();
			tenant.setName(fullName);
			tenant.setEmail("tenant" + seedSuffix + "_" + (emailBase + i - 1) + "@example.com");
			tenant.setPassword(passwordEncoder.encode("Example@123"));
			tenant.setAvatar("https://api.dicebear.com/7.x/avataaars/png?seed=" + fullName.replace(" ", ""));

			tenantRepository.save(tenant);
		}

		System.out.println("TenantSeeder: 100 tenants appended successfully");
	}
}
