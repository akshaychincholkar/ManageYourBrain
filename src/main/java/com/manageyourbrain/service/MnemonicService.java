package com.manageyourbrain.service;

import com.manageyourbrain.domain.Mnemonic;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manageyourbrain.domain.Mnemonic}.
 */
public interface MnemonicService {
    /**
     * Save a mnemonic.
     *
     * @param mnemonic the entity to save.
     * @return the persisted entity.
     */
    Mnemonic save(Mnemonic mnemonic);

    /**
     * Updates a mnemonic.
     *
     * @param mnemonic the entity to update.
     * @return the persisted entity.
     */
    Mnemonic update(Mnemonic mnemonic);

    /**
     * Partially updates a mnemonic.
     *
     * @param mnemonic the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mnemonic> partialUpdate(Mnemonic mnemonic);

    /**
     * Get all the mnemonics.
     *
     * @return the list of entities.
     */
    List<Mnemonic> findAll();

    /**
     * Get the "id" mnemonic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mnemonic> findOne(Long id);

    /**
     * Delete the "id" mnemonic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
