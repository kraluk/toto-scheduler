package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;

import com.kraluk.totoscheduler.domain.TherapyType;
import com.kraluk.totoscheduler.repository.TherapyTypeRepository;
import com.kraluk.totoscheduler.service.TherapyTypeService;
import com.kraluk.totoscheduler.repository.search.TherapyTypeSearchRepository;
import com.kraluk.totoscheduler.service.dto.TherapyTypeDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyTypeMapper;
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
 * Test class for the TherapyTypeResource REST controller.
 *
 * @see TherapyTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class TherapyTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TherapyTypeRepository therapyTypeRepository;

    @Autowired
    private TherapyTypeMapper therapyTypeMapper;

    @Autowired
    private TherapyTypeService therapyTypeService;

    @Autowired
    private TherapyTypeSearchRepository therapyTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTherapyTypeMockMvc;

    private TherapyType therapyType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TherapyTypeResource therapyTypeResource = new TherapyTypeResource(therapyTypeService);
        this.restTherapyTypeMockMvc = MockMvcBuilders.standaloneSetup(therapyTypeResource)
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
    public static TherapyType createEntity(EntityManager em) {
        TherapyType therapyType = new TherapyType()
            .name(DEFAULT_NAME)
            .comment(DEFAULT_COMMENT);
        return therapyType;
    }

    @Before
    public void initTest() {
        therapyTypeSearchRepository.deleteAll();
        therapyType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTherapyType() throws Exception {
        int databaseSizeBeforeCreate = therapyTypeRepository.findAll().size();

        // Create the TherapyType
        TherapyTypeDTO therapyTypeDTO = therapyTypeMapper.toDto(therapyType);
        restTherapyTypeMockMvc.perform(post("/api/therapy-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TherapyType in the database
        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TherapyType testTherapyType = therapyTypeList.get(therapyTypeList.size() - 1);
        assertThat(testTherapyType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTherapyType.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the TherapyType in Elasticsearch
        TherapyType therapyTypeEs = therapyTypeSearchRepository.findOne(testTherapyType.getId());
        assertThat(therapyTypeEs).isEqualToComparingFieldByField(testTherapyType);
    }

    @Test
    @Transactional
    public void createTherapyTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = therapyTypeRepository.findAll().size();

        // Create the TherapyType with an existing ID
        therapyType.setId(1L);
        TherapyTypeDTO therapyTypeDTO = therapyTypeMapper.toDto(therapyType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapyTypeMockMvc.perform(post("/api/therapy-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TherapyType in the database
        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = therapyTypeRepository.findAll().size();
        // set the field null
        therapyType.setName(null);

        // Create the TherapyType, which fails.
        TherapyTypeDTO therapyTypeDTO = therapyTypeMapper.toDto(therapyType);

        restTherapyTypeMockMvc.perform(post("/api/therapy-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTherapyTypes() throws Exception {
        // Initialize the database
        therapyTypeRepository.saveAndFlush(therapyType);

        // Get all the therapyTypeList
        restTherapyTypeMockMvc.perform(get("/api/therapy-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTherapyType() throws Exception {
        // Initialize the database
        therapyTypeRepository.saveAndFlush(therapyType);

        // Get the therapyType
        restTherapyTypeMockMvc.perform(get("/api/therapy-types/{id}", therapyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(therapyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTherapyType() throws Exception {
        // Get the therapyType
        restTherapyTypeMockMvc.perform(get("/api/therapy-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTherapyType() throws Exception {
        // Initialize the database
        therapyTypeRepository.saveAndFlush(therapyType);
        therapyTypeSearchRepository.save(therapyType);
        int databaseSizeBeforeUpdate = therapyTypeRepository.findAll().size();

        // Update the therapyType
        TherapyType updatedTherapyType = therapyTypeRepository.findOne(therapyType.getId());
        updatedTherapyType
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT);
        TherapyTypeDTO therapyTypeDTO = therapyTypeMapper.toDto(updatedTherapyType);

        restTherapyTypeMockMvc.perform(put("/api/therapy-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyTypeDTO)))
            .andExpect(status().isOk());

        // Validate the TherapyType in the database
        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeUpdate);
        TherapyType testTherapyType = therapyTypeList.get(therapyTypeList.size() - 1);
        assertThat(testTherapyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTherapyType.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the TherapyType in Elasticsearch
        TherapyType therapyTypeEs = therapyTypeSearchRepository.findOne(testTherapyType.getId());
        assertThat(therapyTypeEs).isEqualToComparingFieldByField(testTherapyType);
    }

    @Test
    @Transactional
    public void updateNonExistingTherapyType() throws Exception {
        int databaseSizeBeforeUpdate = therapyTypeRepository.findAll().size();

        // Create the TherapyType
        TherapyTypeDTO therapyTypeDTO = therapyTypeMapper.toDto(therapyType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTherapyTypeMockMvc.perform(put("/api/therapy-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(therapyTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TherapyType in the database
        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTherapyType() throws Exception {
        // Initialize the database
        therapyTypeRepository.saveAndFlush(therapyType);
        therapyTypeSearchRepository.save(therapyType);
        int databaseSizeBeforeDelete = therapyTypeRepository.findAll().size();

        // Get the therapyType
        restTherapyTypeMockMvc.perform(delete("/api/therapy-types/{id}", therapyType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean therapyTypeExistsInEs = therapyTypeSearchRepository.exists(therapyType.getId());
        assertThat(therapyTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<TherapyType> therapyTypeList = therapyTypeRepository.findAll();
        assertThat(therapyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTherapyType() throws Exception {
        // Initialize the database
        therapyTypeRepository.saveAndFlush(therapyType);
        therapyTypeSearchRepository.save(therapyType);

        // Search the therapyType
        restTherapyTypeMockMvc.perform(get("/api/_search/therapy-types?query=id:" + therapyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapyType.class);
        TherapyType therapyType1 = new TherapyType();
        therapyType1.setId(1L);
        TherapyType therapyType2 = new TherapyType();
        therapyType2.setId(therapyType1.getId());
        assertThat(therapyType1).isEqualTo(therapyType2);
        therapyType2.setId(2L);
        assertThat(therapyType1).isNotEqualTo(therapyType2);
        therapyType1.setId(null);
        assertThat(therapyType1).isNotEqualTo(therapyType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TherapyTypeDTO.class);
        TherapyTypeDTO therapyTypeDTO1 = new TherapyTypeDTO();
        therapyTypeDTO1.setId(1L);
        TherapyTypeDTO therapyTypeDTO2 = new TherapyTypeDTO();
        assertThat(therapyTypeDTO1).isNotEqualTo(therapyTypeDTO2);
        therapyTypeDTO2.setId(therapyTypeDTO1.getId());
        assertThat(therapyTypeDTO1).isEqualTo(therapyTypeDTO2);
        therapyTypeDTO2.setId(2L);
        assertThat(therapyTypeDTO1).isNotEqualTo(therapyTypeDTO2);
        therapyTypeDTO1.setId(null);
        assertThat(therapyTypeDTO1).isNotEqualTo(therapyTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(therapyTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(therapyTypeMapper.fromId(null)).isNull();
    }
}
