package com.manageyourbrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manageyourbrain.IntegrationTest;
import com.manageyourbrain.domain.Mnemonic;
import com.manageyourbrain.repository.MnemonicRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MnemonicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MnemonicResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mnemonics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MnemonicRepository mnemonicRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMnemonicMockMvc;

    private Mnemonic mnemonic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mnemonic createEntity(EntityManager em) {
        Mnemonic mnemonic = new Mnemonic().name(DEFAULT_NAME).creationDate(DEFAULT_CREATION_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
        return mnemonic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mnemonic createUpdatedEntity(EntityManager em) {
        Mnemonic mnemonic = new Mnemonic().name(UPDATED_NAME).creationDate(UPDATED_CREATION_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
        return mnemonic;
    }

    @BeforeEach
    public void initTest() {
        mnemonic = createEntity(em);
    }

    @Test
    @Transactional
    void createMnemonic() throws Exception {
        int databaseSizeBeforeCreate = mnemonicRepository.findAll().size();
        // Create the Mnemonic
        restMnemonicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mnemonic)))
            .andExpect(status().isCreated());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeCreate + 1);
        Mnemonic testMnemonic = mnemonicList.get(mnemonicList.size() - 1);
        assertThat(testMnemonic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMnemonic.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testMnemonic.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createMnemonicWithExistingId() throws Exception {
        // Create the Mnemonic with an existing ID
        mnemonic.setId(1L);

        int databaseSizeBeforeCreate = mnemonicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMnemonicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mnemonic)))
            .andExpect(status().isBadRequest());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mnemonicRepository.findAll().size();
        // set the field null
        mnemonic.setName(null);

        // Create the Mnemonic, which fails.

        restMnemonicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mnemonic)))
            .andExpect(status().isBadRequest());

        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMnemonics() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        // Get all the mnemonicList
        restMnemonicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mnemonic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMnemonic() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        // Get the mnemonic
        restMnemonicMockMvc
            .perform(get(ENTITY_API_URL_ID, mnemonic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mnemonic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMnemonic() throws Exception {
        // Get the mnemonic
        restMnemonicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMnemonic() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();

        // Update the mnemonic
        Mnemonic updatedMnemonic = mnemonicRepository.findById(mnemonic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMnemonic are not directly saved in db
        em.detach(updatedMnemonic);
        updatedMnemonic.name(UPDATED_NAME).creationDate(UPDATED_CREATION_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restMnemonicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMnemonic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMnemonic))
            )
            .andExpect(status().isOk());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
        Mnemonic testMnemonic = mnemonicList.get(mnemonicList.size() - 1);
        assertThat(testMnemonic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMnemonic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMnemonic.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mnemonic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mnemonic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mnemonic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mnemonic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMnemonicWithPatch() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();

        // Update the mnemonic using partial update
        Mnemonic partialUpdatedMnemonic = new Mnemonic();
        partialUpdatedMnemonic.setId(mnemonic.getId());

        partialUpdatedMnemonic.creationDate(UPDATED_CREATION_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restMnemonicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMnemonic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMnemonic))
            )
            .andExpect(status().isOk());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
        Mnemonic testMnemonic = mnemonicList.get(mnemonicList.size() - 1);
        assertThat(testMnemonic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMnemonic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMnemonic.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMnemonicWithPatch() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();

        // Update the mnemonic using partial update
        Mnemonic partialUpdatedMnemonic = new Mnemonic();
        partialUpdatedMnemonic.setId(mnemonic.getId());

        partialUpdatedMnemonic.name(UPDATED_NAME).creationDate(UPDATED_CREATION_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restMnemonicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMnemonic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMnemonic))
            )
            .andExpect(status().isOk());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
        Mnemonic testMnemonic = mnemonicList.get(mnemonicList.size() - 1);
        assertThat(testMnemonic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMnemonic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMnemonic.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mnemonic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mnemonic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mnemonic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMnemonic() throws Exception {
        int databaseSizeBeforeUpdate = mnemonicRepository.findAll().size();
        mnemonic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMnemonicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mnemonic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mnemonic in the database
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMnemonic() throws Exception {
        // Initialize the database
        mnemonicRepository.saveAndFlush(mnemonic);

        int databaseSizeBeforeDelete = mnemonicRepository.findAll().size();

        // Delete the mnemonic
        restMnemonicMockMvc
            .perform(delete(ENTITY_API_URL_ID, mnemonic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mnemonic> mnemonicList = mnemonicRepository.findAll();
        assertThat(mnemonicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
