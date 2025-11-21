package com.rental_pg_backend.manager;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.common.services.CloudinaryService;
import com.rental_pg_backend.manager.dto.CreateManagerDto;
import com.rental_pg_backend.manager.dto.ManagerDto;
import com.rental_pg_backend.manager.entities.Manager;
import com.rental_pg_backend.manager.repositories.ManagerRepository;
import com.rental_pg_backend.property.entities.Property;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    private final CloudinaryService cloudinaryService;

    public ManagerDto findById(UUID id) {
        return managerRepository.findById(id).map(managerMapper::toDto).orElse(null);
    }

    public ManagerDto findByEmail(String email) {
        return managerRepository.findByEmail(email).map(managerMapper::toDto)
                .orElse(null);
    }

    public ManagerDto createAccount(CreateManagerDto createManagerDto) {

        Manager manager = managerMapper.fromCreateDto(createManagerDto);

        if (Objects.nonNull(createManagerDto.getAvatar())) {
            String uploadedAvatarUrl = cloudinaryService.uploadFile(createManagerDto.getAvatar());
            manager.setAvatar(uploadedAvatarUrl);
        }

        Manager createdAccount = managerRepository.save(manager);

        return managerMapper.toDto(createdAccount);

    }

    public void saveProperty(UUID id, Property property) {

        UUID managerId = managerRepository.findIdById(id);

        if (Objects.isNull(managerId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No manager found with this id");

        Manager manager = new Manager();

        manager.setId(managerId);

        property.setManager(manager);

    }

    public Set<UUID> getManagedPropertyIds(UUID managerId) {
        return managerRepository.findManagedPropertyIdsByManagerId(managerId);
    }

}
