package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;
import com.kraluk.totoscheduler.domain.TimeTable;
import com.kraluk.totoscheduler.repository.TimeTableRepository;
import com.kraluk.totoscheduler.service.TimeTableService;
import com.kraluk.totoscheduler.service.dto.TimeTableDto;
import com.kraluk.totoscheduler.service.mapper.TimeTableMapper;
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

import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the TimeTableResource REST controller.
 *
 * @see TimeTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class TimeTableResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTableMapper timeTableMapper;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeTableMockMvc;

    private TimeTable timeTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimeTableResource timeTableResource = new TimeTableResource(timeTableService);
        this.restTimeTableMockMvc = MockMvcBuilders.standaloneSetup(timeTableResource)
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
    public static TimeTable createEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .comment(DEFAULT_COMMENT);
        return timeTable;
    }

    @Before
    public void initTest() {
        timeTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeTable() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();

        // Create the TimeTable
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);
        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isCreated());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate + 1);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTimeTable.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTimeTable.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTimeTable.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createTimeTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();

        // Create the TimeTable with an existing ID
        timeTable.setId(1L);
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setName(null);

        // Create the TimeTable, which fails.
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setStartDate(null);

        // Create the TimeTable, which fails.
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setEndDate(null);

        // Create the TimeTable, which fails.
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimeTables() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get all the timeTableList
        restTimeTableMockMvc.perform(get("/api/time-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get the timeTable
        restTimeTableMockMvc.perform(get("/api/time-tables/{id}", timeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeTable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimeTable() throws Exception {
        // Get the timeTable
        restTimeTableMockMvc.perform(get("/api/time-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Update the timeTable
        TimeTable updatedTimeTable = timeTableRepository.findOne(timeTable.getId());
        updatedTimeTable
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .comment(UPDATED_COMMENT);
        TimeTableDto timeTableDto = timeTableMapper.toDto(updatedTimeTable);

        restTimeTableMockMvc.perform(put("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isOk());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimeTable.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTimeTable.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTimeTable.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Create the TimeTable
        TimeTableDto timeTableDto = timeTableMapper.toDto(timeTable);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeTableMockMvc.perform(put("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTableDto)))
            .andExpect(status().isCreated());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);
        int databaseSizeBeforeDelete = timeTableRepository.findAll().size();

        // Get the timeTable
        restTimeTableMockMvc.perform(delete("/api/time-tables/{id}", timeTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeTable.class);
        TimeTable timeTable1 = new TimeTable();
        timeTable1.setId(1L);
        TimeTable timeTable2 = new TimeTable();
        timeTable2.setId(timeTable1.getId());
        assertThat(timeTable1).isEqualTo(timeTable2);
        timeTable2.setId(2L);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
        timeTable1.setId(null);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeTableDto.class);
        TimeTableDto timeTableDto1 = new TimeTableDto();
        timeTableDto1.setId(1L);
        TimeTableDto timeTableDto2 = new TimeTableDto();
        assertThat(timeTableDto1).isNotEqualTo(timeTableDto2);
        timeTableDto2.setId(timeTableDto1.getId());
        assertThat(timeTableDto1).isEqualTo(timeTableDto2);
        timeTableDto2.setId(2L);
        assertThat(timeTableDto1).isNotEqualTo(timeTableDto2);
        timeTableDto1.setId(null);
        assertThat(timeTableDto1).isNotEqualTo(timeTableDto2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(timeTableMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(timeTableMapper.fromId(null)).isNull();
    }
}
