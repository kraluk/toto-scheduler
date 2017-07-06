package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;

import com.kraluk.totoscheduler.domain.Period;
import com.kraluk.totoscheduler.repository.PeriodRepository;
import com.kraluk.totoscheduler.service.dto.PeriodDTO;
import com.kraluk.totoscheduler.service.mapper.PeriodMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PeriodResource REST controller.
 *
 * @see PeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class PeriodResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PeriodMapper periodMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeriodMockMvc;

    private Period period;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeriodResource periodResource = new PeriodResource(periodRepository, periodMapper);
        this.restPeriodMockMvc = MockMvcBuilders.standaloneSetup(periodResource)
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
    public static Period createEntity(EntityManager em) {
        Period period = new Period()
            .name(DEFAULT_NAME)
            .comment(DEFAULT_COMMENT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return period;
    }

    @Before
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriod() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeriod.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createPeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period with an existing ID
        period.setId(1L);
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setName(null);

        // Create the Period, which fails.
        PeriodDTO periodDTO = periodMapper.toDto(period);

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setStartDate(null);

        // Create the Period, which fails.
        PeriodDTO periodDTO = periodMapper.toDto(period);

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setEndDate(null);

        // Create the Period, which fails.
        PeriodDTO periodDTO = periodMapper.toDto(period);

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period
        Period updatedPeriod = periodRepository.findOne(period.getId());
        updatedPeriod
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        PeriodDTO periodDTO = periodMapper.toDto(updatedPeriod);

        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeriod.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodDTO)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);
        int databaseSizeBeforeDelete = periodRepository.findAll().size();

        // Get the period
        restPeriodMockMvc.perform(delete("/api/periods/{id}", period.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
        Period period1 = new Period();
        period1.setId(1L);
        Period period2 = new Period();
        period2.setId(period1.getId());
        assertThat(period1).isEqualTo(period2);
        period2.setId(2L);
        assertThat(period1).isNotEqualTo(period2);
        period1.setId(null);
        assertThat(period1).isNotEqualTo(period2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodDTO.class);
        PeriodDTO periodDTO1 = new PeriodDTO();
        periodDTO1.setId(1L);
        PeriodDTO periodDTO2 = new PeriodDTO();
        assertThat(periodDTO1).isNotEqualTo(periodDTO2);
        periodDTO2.setId(periodDTO1.getId());
        assertThat(periodDTO1).isEqualTo(periodDTO2);
        periodDTO2.setId(2L);
        assertThat(periodDTO1).isNotEqualTo(periodDTO2);
        periodDTO1.setId(null);
        assertThat(periodDTO1).isNotEqualTo(periodDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(periodMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(periodMapper.fromId(null)).isNull();
    }
}
