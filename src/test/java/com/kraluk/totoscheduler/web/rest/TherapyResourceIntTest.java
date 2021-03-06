package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;
import com.kraluk.totoscheduler.domain.Therapy;
import com.kraluk.totoscheduler.repository.TherapyRepository;
import com.kraluk.totoscheduler.repository.search.TherapySearchRepository;
import com.kraluk.totoscheduler.service.TherapyService;
import com.kraluk.totoscheduler.service.dto.TherapyDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyMapper;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import static com.kraluk.totoscheduler.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the TherapyResource REST controller.
 *
 * @see TherapyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class TherapyResourceIntTest {

    private static final ZonedDateTime
        DEFAULT_DATE =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime
        UPDATED_DATE =
        ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TherapyRepository therapyRepository;

    @Autowired
    private TherapyMapper therapyMapper;

    @Autowired
    private TherapyService therapyService;

    @Autowired
    private TherapySearchRepository therapySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTherapyMockMvc;

    private Therapy therapy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TherapyResource therapyResource = new TherapyResource(therapyService);
        this.restTherapyMockMvc = MockMvcBuilders.standaloneSetup(therapyResource)
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
    public static Therapy createEntity(EntityManager em) {
        Therapy therapy = new Therapy()
            .date(DEFAULT_DATE)
            .comment(DEFAULT_COMMENT);
        return therapy;
    }

    @Before
    public void initTest() {
        therapySearchRepository.deleteAll();
        therapy = createEntity(em);
    }

    @Test
    @Transactional
    public void createTherapy() throws Exception {
        int databaseSizeBeforeCreate = therapyRepository.findAll().size();

        // Create the Therapy
        TherapyDTO therapyDTO = therapyMapper.toDto(therapy);
        restTherapyMockMvc.perform(post("/api/therapies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyDTO)))
            .andExpect(status().isCreated());

        // Validate the Therapy in the database
        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeCreate + 1);
        Therapy testTherapy = therapyList.get(therapyList.size() - 1);
        assertThat(testTherapy.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTherapy.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the Therapy in Elasticsearch
        Therapy therapyEs = therapySearchRepository.findOne(testTherapy.getId());
        assertThat(therapyEs).isEqualToComparingFieldByField(testTherapy);
    }

    @Test
    @Transactional
    public void createTherapyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = therapyRepository.findAll().size();

        // Create the Therapy with an existing ID
        therapy.setId(1L);
        TherapyDTO therapyDTO = therapyMapper.toDto(therapy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapyMockMvc.perform(post("/api/therapies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Therapy in the database
        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapyRepository.findAll().size();
        // set the field null
        therapy.setDate(null);

        // Create the Therapy, which fails.
        TherapyDTO therapyDTO = therapyMapper.toDto(therapy);

        restTherapyMockMvc.perform(post("/api/therapies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyDTO)))
            .andExpect(status().isBadRequest());

        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTherapies() throws Exception {
        // Initialize the database
        therapyRepository.saveAndFlush(therapy);

        // Get all the therapyList
        restTherapyMockMvc.perform(get("/api/therapies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapy.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTherapy() throws Exception {
        // Initialize the database
        therapyRepository.saveAndFlush(therapy);

        // Get the therapy
        restTherapyMockMvc.perform(get("/api/therapies/{id}", therapy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(therapy.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTherapy() throws Exception {
        // Get the therapy
        restTherapyMockMvc.perform(get("/api/therapies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTherapy() throws Exception {
        // Initialize the database
        therapyRepository.saveAndFlush(therapy);
        therapySearchRepository.save(therapy);
        int databaseSizeBeforeUpdate = therapyRepository.findAll().size();

        // Update the therapy
        Therapy updatedTherapy = therapyRepository.findOne(therapy.getId());
        updatedTherapy
            .date(UPDATED_DATE)
            .comment(UPDATED_COMMENT);
        TherapyDTO therapyDTO = therapyMapper.toDto(updatedTherapy);

        restTherapyMockMvc.perform(put("/api/therapies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyDTO)))
            .andExpect(status().isOk());

        // Validate the Therapy in the database
        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeUpdate);
        Therapy testTherapy = therapyList.get(therapyList.size() - 1);
        assertThat(testTherapy.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTherapy.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the Therapy in Elasticsearch
        Therapy therapyEs = therapySearchRepository.findOne(testTherapy.getId());
        assertThat(therapyEs).isEqualToComparingFieldByField(testTherapy);
    }

    @Test
    @Transactional
    public void updateNonExistingTherapy() throws Exception {
        int databaseSizeBeforeUpdate = therapyRepository.findAll().size();

        // Create the Therapy
        TherapyDTO therapyDTO = therapyMapper.toDto(therapy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTherapyMockMvc.perform(put("/api/therapies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyDTO)))
            .andExpect(status().isCreated());

        // Validate the Therapy in the database
        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTherapy() throws Exception {
        // Initialize the database
        therapyRepository.saveAndFlush(therapy);
        therapySearchRepository.save(therapy);
        int databaseSizeBeforeDelete = therapyRepository.findAll().size();

        // Get the therapy
        restTherapyMockMvc.perform(delete("/api/therapies/{id}", therapy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean therapyExistsInEs = therapySearchRepository.exists(therapy.getId());
        assertThat(therapyExistsInEs).isFalse();

        // Validate the database is empty
        List<Therapy> therapyList = therapyRepository.findAll();
        assertThat(therapyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTherapy() throws Exception {
        // Initialize the database
        therapyRepository.saveAndFlush(therapy);
        therapySearchRepository.save(therapy);

        // Search the therapy
        restTherapyMockMvc.perform(get("/api/_search/therapies?query=id:" + therapy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapy.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Therapy.class);
        Therapy therapy1 = new Therapy();
        therapy1.setId(1L);
        Therapy therapy2 = new Therapy();
        therapy2.setId(therapy1.getId());
        assertThat(therapy1).isEqualTo(therapy2);
        therapy2.setId(2L);
        assertThat(therapy1).isNotEqualTo(therapy2);
        therapy1.setId(null);
        assertThat(therapy1).isNotEqualTo(therapy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapyDTO.class);
        TherapyDTO therapyDTO1 = new TherapyDTO();
        therapyDTO1.setId(1L);
        TherapyDTO therapyDTO2 = new TherapyDTO();
        assertThat(therapyDTO1).isNotEqualTo(therapyDTO2);
        therapyDTO2.setId(therapyDTO1.getId());
        assertThat(therapyDTO1).isEqualTo(therapyDTO2);
        therapyDTO2.setId(2L);
        assertThat(therapyDTO1).isNotEqualTo(therapyDTO2);
        therapyDTO1.setId(null);
        assertThat(therapyDTO1).isNotEqualTo(therapyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(therapyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(therapyMapper.fromId(null)).isNull();
    }
}
