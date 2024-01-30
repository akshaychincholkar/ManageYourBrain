package com.manageyourbrain.web.rest;

import com.manageyourbrain.domain.Mnemonic;
import com.manageyourbrain.repository.MnemonicRepository;
import com.manageyourbrain.service.MnemonicService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.manageyourbrain.domain.Mnemonic}.
 */
@RestController
@RequestMapping("/api/mnemonics")
public class MnemonicResource {

    private final Logger log = LoggerFactory.getLogger(MnemonicResource.class);

    private static final String ENTITY_NAME = "mnemonic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MnemonicService mnemonicService;

    private final MnemonicRepository mnemonicRepository;

    public MnemonicResource(MnemonicService mnemonicService, MnemonicRepository mnemonicRepository) {
        this.mnemonicService = mnemonicService;
        this.mnemonicRepository = mnemonicRepository;
    }

    /**
     * {@code POST  /mnemonics} : Create a new mnemonic.
     *
     * @param mnemonic the mnemonic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mnemonic, or with status {@code 400 (Bad Request)} if the mnemonic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Mnemonic> createMnemonic(@Valid @RequestBody Mnemonic mnemonic) throws URISyntaxException {
        log.debug("REST request to save Mnemonic : {}", mnemonic);
        if (mnemonic.getId() != null) {
            throw new BadRequestAlertException("A new mnemonic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mnemonic result = mnemonicService.save(mnemonic);
        return ResponseEntity
            .created(new URI("/api/mnemonics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mnemonics/:id} : Updates an existing mnemonic.
     *
     * @param id the id of the mnemonic to save.
     * @param mnemonic the mnemonic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mnemonic,
     * or with status {@code 400 (Bad Request)} if the mnemonic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mnemonic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mnemonic> updateMnemonic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mnemonic mnemonic
    ) throws URISyntaxException {
        log.debug("REST request to update Mnemonic : {}, {}", id, mnemonic);
        if (mnemonic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mnemonic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mnemonicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mnemonic result = mnemonicService.update(mnemonic);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mnemonic.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mnemonics/:id} : Partial updates given fields of an existing mnemonic, field will ignore if it is null
     *
     * @param id the id of the mnemonic to save.
     * @param mnemonic the mnemonic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mnemonic,
     * or with status {@code 400 (Bad Request)} if the mnemonic is not valid,
     * or with status {@code 404 (Not Found)} if the mnemonic is not found,
     * or with status {@code 500 (Internal Server Error)} if the mnemonic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mnemonic> partialUpdateMnemonic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mnemonic mnemonic
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mnemonic partially : {}, {}", id, mnemonic);
        if (mnemonic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mnemonic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mnemonicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mnemonic> result = mnemonicService.partialUpdate(mnemonic);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mnemonic.getId().toString())
        );
    }

    /**
     * {@code GET  /mnemonics} : get all the mnemonics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mnemonics in body.
     */
    @GetMapping("")
    public List<Mnemonic> getAllMnemonics() {
        log.debug("REST request to get all Mnemonics");
        return mnemonicService.findAll();
    }

    /**
     * {@code GET  /mnemonics/:id} : get the "id" mnemonic.
     *
     * @param id the id of the mnemonic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mnemonic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mnemonic> getMnemonic(@PathVariable("id") Long id) {
        log.debug("REST request to get Mnemonic : {}", id);
        Optional<Mnemonic> mnemonic = mnemonicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mnemonic);
    }

    /**
     * {@code DELETE  /mnemonics/:id} : delete the "id" mnemonic.
     *
     * @param id the id of the mnemonic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMnemonic(@PathVariable("id") Long id) {
        log.debug("REST request to delete Mnemonic : {}", id);
        mnemonicService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
