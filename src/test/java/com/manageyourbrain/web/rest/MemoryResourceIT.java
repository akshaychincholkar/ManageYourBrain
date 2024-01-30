package com.manageyourbrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manageyourbrain.IntegrationTest;
import com.manageyourbrain.domain.Memory;
import com.manageyourbrain.repository.MemoryRepository;
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
 * Integration tests for the {@link MemoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemoryResourceIT {

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final Instant DEFAULT_LEARNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LEARNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/memories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemoryMockMvc;

    private Memory memory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createEntity(EntityManager em) {
        Memory memory = new Memory()
            .topic(DEFAULT_TOPIC)
            .learnedDate(DEFAULT_LEARNED_DATE)
            .key(DEFAULT_KEY)
            .comment(DEFAULT_COMMENT)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return memory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createUpdatedEntity(EntityManager em) {
        Memory memory = new Memory()
            .topic(UPDATED_TOPIC)
            .learnedDate(UPDATED_LEARNED_DATE)
            .key(UPDATED_KEY)
            .comment(UPDATED_COMMENT)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return memory;
    }

    @BeforeEach
    public void initTest() {
        memory = createEntity(em);
    }

    @Test
    @Transactional
    void createMemory() throws Exception {
        int databaseSizeBeforeCreate = memoryRepository.findAll().size();
        // Create the Memory
        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isCreated());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate + 1);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testMemory.getLearnedDate()).isEqualTo(DEFAULT_LEARNED_DATE);
        assertThat(testMemory.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testMemory.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testMemory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testMemory.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createMemoryWithExistingId() throws Exception {
        // Create the Memory with an existing ID
        memory.setId(1L);

        int databaseSizeBeforeCreate = memoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = memoryRepository.findAll().size();
        // set the field null
        memory.setTopic(null);

        // Create the Memory, which fails.

        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isBadRequest());

        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLearnedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = memoryRepository.findAll().size();
        // set the field null
        memory.setLearnedDate(null);

        // Create the Memory, which fails.

        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isBadRequest());

        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = memoryRepository.findAll().size();
        // set the field null
        memory.setKey(null);

        // Create the Memory, which fails.

        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isBadRequest());

        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemories() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList
        restMemoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memory.getId().intValue())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].learnedDate").value(hasItem(DEFAULT_LEARNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get the memory
        restMemoryMockMvc
            .perform(get(ENTITY_API_URL_ID, memory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memory.getId().intValue()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.learnedDate").value(DEFAULT_LEARNED_DATE.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMemory() throws Exception {
        // Get the memory
        restMemoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory
        Memory updatedMemory = memoryRepository.findById(memory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMemory are not directly saved in db
        em.detach(updatedMemory);
        updatedMemory
            .topic(UPDATED_TOPIC)
            .learnedDate(UPDATED_LEARNED_DATE)
            .key(UPDATED_KEY)
            .comment(UPDATED_COMMENT)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMemory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testMemory.getLearnedDate()).isEqualTo(UPDATED_LEARNED_DATE);
        assertThat(testMemory.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testMemory.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testMemory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMemory.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.topic(UPDATED_TOPIC).learnedDate(UPDATED_LEARNED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testMemory.getLearnedDate()).isEqualTo(UPDATED_LEARNED_DATE);
        assertThat(testMemory.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testMemory.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testMemory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testMemory.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory
            .topic(UPDATED_TOPIC)
            .learnedDate(UPDATED_LEARNED_DATE)
            .key(UPDATED_KEY)
            .comment(UPDATED_COMMENT)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testMemory.getLearnedDate()).isEqualTo(UPDATED_LEARNED_DATE);
        assertThat(testMemory.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testMemory.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testMemory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMemory.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();
        memory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(memory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeDelete = memoryRepository.findAll().size();

        // Delete the memory
        restMemoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, memory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
