package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;

import com.kraluk.totoscheduler.domain.TherapyEntry;
import com.kraluk.totoscheduler.repository.TherapyEntryRepository;
import com.kraluk.totoscheduler.service.dto.TherapyEntryDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyEntryMapper;
import com.kraluk.totoscheduler.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TherapyEntryResource REST controller.
 *
 * @see TherapyEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class TherapyEntryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TherapyEntryRepository therapyEntryRepository;

    @Autowired
    private TherapyEntryMapper therapyEntryMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTherapyEntryMockMvc;

    private TherapyEntry therapyEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TherapyEntryResource therapyEntryResource = new TherapyEntryResource(therapyEntryRepository, therapyEntryMapper);
        this.restTherapyEntryMockMvc = MockMvcBuilders.standaloneSetup(therapyEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TherapyEntry createEntity(EntityManager em) {
        TherapyEntry therapyEntry = new TherapyEntry()
            .name(DEFAULT_NAME);
        return therapyEntry;
    }

    @Before
    public void initTest() {
        therapyEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createTherapyEntry() throws Exception {
        int databaseSizeBeforeCreate = therapyEntryRepository.findAll().size();

        // Create the TherapyEntry
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(therapyEntry);
        restTherapyEntryMockMvc.perform(post("/api/therapy-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the TherapyEntry in the database
        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeCreate + 1);
        TherapyEntry testTherapyEntry = therapyEntryList.get(therapyEntryList.size() - 1);
        assertThat(testTherapyEntry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTherapyEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = therapyEntryRepository.findAll().size();

        // Create the TherapyEntry with an existing ID
        therapyEntry.setId(1L);
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(therapyEntry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapyEntryMockMvc.perform(post("/api/therapy-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapyEntryRepository.findAll().size();
        // set the field null
        therapyEntry.setName(null);

        // Create the TherapyEntry, which fails.
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(therapyEntry);

        restTherapyEntryMockMvc.perform(post("/api/therapy-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyEntryDTO)))
            .andExpect(status().isBadRequest());

        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTherapyEntries() throws Exception {
        // Initialize the database
        therapyEntryRepository.saveAndFlush(therapyEntry);

        // Get all the therapyEntryList
        restTherapyEntryMockMvc.perform(get("/api/therapy-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapyEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTherapyEntry() throws Exception {
        // Initialize the database
        therapyEntryRepository.saveAndFlush(therapyEntry);

        // Get the therapyEntry
        restTherapyEntryMockMvc.perform(get("/api/therapy-entries/{id}", therapyEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(therapyEntry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTherapyEntry() throws Exception {
        // Get the therapyEntry
        restTherapyEntryMockMvc.perform(get("/api/therapy-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTherapyEntry() throws Exception {
        // Initialize the database
        therapyEntryRepository.saveAndFlush(therapyEntry);
        int databaseSizeBeforeUpdate = therapyEntryRepository.findAll().size();

        // Update the therapyEntry
        TherapyEntry updatedTherapyEntry = therapyEntryRepository.findOne(therapyEntry.getId());
        updatedTherapyEntry
            .name(UPDATED_NAME);
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(updatedTherapyEntry);

        restTherapyEntryMockMvc.perform(put("/api/therapy-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyEntryDTO)))
            .andExpect(status().isOk());

        // Validate the TherapyEntry in the database
        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeUpdate);
        TherapyEntry testTherapyEntry = therapyEntryList.get(therapyEntryList.size() - 1);
        assertThat(testTherapyEntry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTherapyEntry() throws Exception {
        int databaseSizeBeforeUpdate = therapyEntryRepository.findAll().size();

        // Create the TherapyEntry
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(therapyEntry);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTherapyEntryMockMvc.perform(put("/api/therapy-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the TherapyEntry in the database
        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTherapyEntry() throws Exception {
        // Initialize the database
        therapyEntryRepository.saveAndFlush(therapyEntry);
        int databaseSizeBeforeDelete = therapyEntryRepository.findAll().size();

        // Get the therapyEntry
        restTherapyEntryMockMvc.perform(delete("/api/therapy-entries/{id}", therapyEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TherapyEntry> therapyEntryList = therapyEntryRepository.findAll();
        assertThat(therapyEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapyEntry.class);
        TherapyEntry therapyEntry1 = new TherapyEntry();
        therapyEntry1.setId(1L);
        TherapyEntry therapyEntry2 = new TherapyEntry();
        therapyEntry2.setId(therapyEntry1.getId());
        assertThat(therapyEntry1).isEqualTo(therapyEntry2);
        therapyEntry2.setId(2L);
        assertThat(therapyEntry1).isNotEqualTo(therapyEntry2);
        therapyEntry1.setId(null);
        assertThat(therapyEntry1).isNotEqualTo(therapyEntry2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapyEntryDTO.class);
        TherapyEntryDTO therapyEntryDTO1 = new TherapyEntryDTO();
        therapyEntryDTO1.setId(1L);
        TherapyEntryDTO therapyEntryDTO2 = new TherapyEntryDTO();
        assertThat(therapyEntryDTO1).isNotEqualTo(therapyEntryDTO2);
        therapyEntryDTO2.setId(therapyEntryDTO1.getId());
        assertThat(therapyEntryDTO1).isEqualTo(therapyEntryDTO2);
        therapyEntryDTO2.setId(2L);
        assertThat(therapyEntryDTO1).isNotEqualTo(therapyEntryDTO2);
        therapyEntryDTO1.setId(null);
        assertThat(therapyEntryDTO1).isNotEqualTo(therapyEntryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(therapyEntryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(therapyEntryMapper.fromId(null)).isNull();
    }
}
