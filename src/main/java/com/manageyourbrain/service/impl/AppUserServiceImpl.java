package com.manageyourbrain.service.impl;

import com.manageyourbrain.domain.AppUser;
import com.manageyourbrain.repository.AppUserRepository;
import com.manageyourbrain.service.AppUserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.manageyourbrain.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser update(AppUser appUser) {
        log.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        log.debug("Request to partially update AppUser : {}", appUser);

        return appUserRepository
            .findById(appUser.getId())
            .map(existingAppUser -> {
                if (appUser.getName() != null) {
                    existingAppUser.setName(appUser.getName());
                }
                if (appUser.getEmail() != null) {
                    existingAppUser.setEmail(appUser.getEmail());
                }

                return existingAppUser;
            })
            .map(appUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUser> findAll() {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
