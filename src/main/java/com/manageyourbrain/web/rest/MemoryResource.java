package com.manageyourbrain.web.rest;

import com.manageyourbrain.domain.Memory;
import com.manageyourbrain.repository.MemoryRepository;
import com.manageyourbrain.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.manageyourbrain.domain.Memory}.
 */
@RestController
@RequestMapping("/api/memories")
@Transactional
public class MemoryResource {

    private final Logger log = LoggerFactory.getLogger(MemoryResource.class);

    private static final String ENTITY_NAME = "memory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoryRepository memoryRepository;

    public MemoryResource(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    /**
     * {@code POST  /memories} : Create a new memory.
     *
     * @param memory the memory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memory, or with status {@code 400 (Bad Request)} if the memory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Memory> createMemory(@Valid @RequestBody Memory memory) throws URISyntaxException {
        log.debug("REST request to save Memory : {}", memory);
        if (memory.getId() != null) {
            throw new BadRequestAlertException("A new memory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Memory result = memoryRepository.save(memory);
        return ResponseEntity
            .created(new URI("/api/memories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /memories/:id} : Updates an existing memory.
     *
     * @param id the id of the memory to save.
     * @param memory the memory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memory,
     * or with status {@code 400 (Bad Request)} if the memory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Memory> updateMemory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Memory memory
    ) throws URISyntaxException {
        log.debug("REST request to update Memory : {}, {}", id, memory);
        if (memory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Memory result = memoryRepository.save(memory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /memories/:id} : Partial updates given fields of an existing memory, field will ignore if it is null
     *
     * @param id the id of the memory to save.
     * @param memory the memory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memory,
     * or with status {@code 400 (Bad Request)} if the memory is not valid,
     * or with status {@code 404 (Not Found)} if the memory is not found,
     * or with status {@code 500 (Internal Server Error)} if the memory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Memory> partialUpdateMemory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Memory memory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Memory partially : {}, {}", id, memory);
        if (memory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Memory> result = memoryRepository
            .findById(memory.getId())
            .map(existingMemory -> {
                if (memory.getTopic() != null) {
                    existingMemory.setTopic(memory.getTopic());
                }
                if (memory.getLearnedDate() != null) {
                    existingMemory.setLearnedDate(memory.getLearnedDate());
                }
                if (memory.getKey() != null) {
                    existingMemory.setKey(memory.getKey());
                }
                if (memory.getComment() != null) {
                    existingMemory.setComment(memory.getComment());
                }
                if (memory.getCreationDate() != null) {
                    existingMemory.setCreationDate(memory.getCreationDate());
                }
                if (memory.getModifiedDate() != null) {
                    existingMemory.setModifiedDate(memory.getModifiedDate());
                }

                return existingMemory;
            })
            .map(memoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memory.getId().toString())
        );
    }

    /**
     * {@code GET  /memories} : get all the memories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Memory>> getAllMemories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Memories");
        Page<Memory> page = memoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memories/:id} : get the "id" memory.
     *
     * @param id the id of the memory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Memory> getMemory(@PathVariable("id") Long id) {
        log.debug("REST request to get Memory : {}", id);
        Optional<Memory> memory = memoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(memory);
    }

    /**
     * {@code DELETE  /memories/:id} : delete the "id" memory.
     *
     * @param id the id of the memory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable("id") Long id) {
        log.debug("REST request to delete Memory : {}", id);
        memoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
