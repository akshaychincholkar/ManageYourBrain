package com.manageyourbrain.service;

import com.manageyourbrain.domain.AppUser;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manageyourbrain.domain.AppUser}.
 */
public interface AppUserService {
    /**
     * Save a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
     */
    AppUser save(AppUser appUser);

    /**
     * Updates a appUser.
     *
     * @param appUser the entity to update.
     * @return the persisted entity.
     */
    AppUser update(AppUser appUser);

    /**
     * Partially updates a appUser.
     *
     * @param appUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUser> partialUpdate(AppUser appUser);

    /**
     * Get all the appUsers.
     *
     * @return the list of entities.
     */
    List<AppUser> findAll();

    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUser> findOne(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
