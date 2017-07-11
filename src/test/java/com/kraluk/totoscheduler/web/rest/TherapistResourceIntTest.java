package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;
import com.kraluk.totoscheduler.domain.Therapist;
import com.kraluk.totoscheduler.repository.TherapistRepository;
import com.kraluk.totoscheduler.service.dto.TherapistDto;
import com.kraluk.totoscheduler.service.mapper.TherapistMapper;
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

import java.util.List;

import javax.persistence.EntityManager;

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
 * Test class for the TherapistResource REST controller.
 *
 * @see TherapistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class TherapistResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private TherapistMapper therapistMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTherapistMockMvc;

    private Therapist therapist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TherapistResource
            therapistResource =
            new TherapistResource(therapistRepository, therapistMapper);
        this.restTherapistMockMvc = MockMvcBuilders.standaloneSetup(therapistResource)
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
    public static Therapist createEntity(EntityManager em) {
        Therapist therapist = new Therapist()
            .title(DEFAULT_TITLE)
            .comment(DEFAULT_COMMENT);
        return therapist;
    }

    @Before
    public void initTest() {
        therapist = createEntity(em);
    }

    @Test
    @Transactional
    public void createTherapist() throws Exception {
        int databaseSizeBeforeCreate = therapistRepository.findAll().size();

        // Create the Therapist
        TherapistDto therapistDto = therapistMapper.toDto(therapist);
        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapistDto)))
            .andExpect(status().isCreated());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate + 1);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTherapist.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createTherapistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = therapistRepository.findAll().size();

        // Create the Therapist with an existing ID
        therapist.setId(1L);
        TherapistDto therapistDto = therapistMapper.toDto(therapist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapistMockMvc.perform(post("/api/therapists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapistDto)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTherapists() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList
        restTherapistMockMvc.perform(get("/api/therapists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get the therapist
        restTherapistMockMvc.perform(get("/api/therapists/{id}", therapist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(therapist.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTherapist() throws Exception {
        // Get the therapist
        restTherapistMockMvc.perform(get("/api/therapists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Update the therapist
        Therapist updatedTherapist = therapistRepository.findOne(therapist.getId());
        updatedTherapist
            .title(UPDATED_TITLE)
            .comment(UPDATED_COMMENT);
        TherapistDto therapistDto = therapistMapper.toDto(updatedTherapist);

        restTherapistMockMvc.perform(put("/api/therapists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapistDto)))
            .andExpect(status().isOk());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTherapist.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Create the Therapist
        TherapistDto therapistDto = therapistMapper.toDto(therapist);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTherapistMockMvc.perform(put("/api/therapists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapistDto)))
            .andExpect(status().isCreated());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);
        int databaseSizeBeforeDelete = therapistRepository.findAll().size();

        // Get the therapist
        restTherapistMockMvc.perform(delete("/api/therapists/{id}", therapist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Therapist.class);
        Therapist therapist1 = new Therapist();
        therapist1.setId(1L);
        Therapist therapist2 = new Therapist();
        therapist2.setId(therapist1.getId());
        assertThat(therapist1).isEqualTo(therapist2);
        therapist2.setId(2L);
        assertThat(therapist1).isNotEqualTo(therapist2);
        therapist1.setId(null);
        assertThat(therapist1).isNotEqualTo(therapist2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapistDto.class);
        TherapistDto therapistDto1 = new TherapistDto();
        therapistDto1.setId(1L);
        TherapistDto therapistDto2 = new TherapistDto();
        assertThat(therapistDto1).isNotEqualTo(therapistDto2);
        therapistDto2.setId(therapistDto1.getId());
        assertThat(therapistDto1).isEqualTo(therapistDto2);
        therapistDto2.setId(2L);
        assertThat(therapistDto1).isNotEqualTo(therapistDto2);
        therapistDto1.setId(null);
        assertThat(therapistDto1).isNotEqualTo(therapistDto2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(therapistMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(therapistMapper.fromId(null)).isNull();
    }
}
