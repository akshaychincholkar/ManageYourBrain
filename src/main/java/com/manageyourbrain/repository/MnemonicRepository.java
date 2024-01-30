package com.manageyourbrain.repository;

import com.manageyourbrain.domain.Mnemonic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mnemonic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MnemonicRepository extends JpaRepository<Mnemonic, Long> {}
