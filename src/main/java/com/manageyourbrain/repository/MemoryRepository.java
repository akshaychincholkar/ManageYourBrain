package com.manageyourbrain.repository;

import com.manageyourbrain.domain.Memory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Memory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {}
