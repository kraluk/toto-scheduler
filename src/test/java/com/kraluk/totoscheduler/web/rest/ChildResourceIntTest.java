package com.kraluk.totoscheduler.web.rest;

import com.kraluk.totoscheduler.TotoSchedulerApp;

import com.kraluk.totoscheduler.domain.Child;
import com.kraluk.totoscheduler.repository.ChildRepository;
import com.kraluk.totoscheduler.service.ChildService;
import com.kraluk.totoscheduler.repository.search.ChildSearchRepository;
import com.kraluk.totoscheduler.service.dto.ChildDTO;
import com.kraluk.totoscheduler.service.mapper.ChildMapper;
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
 * Test class for the ChildResource REST controller.
 *
 * @see ChildResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TotoSchedulerApp.class)
public class ChildResourceIntTest {

    private static final String DEFAULT_REGISTER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private ChildService childService;

    @Autowired
    private ChildSearchRepository childSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChildMockMvc;

    private Child child;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChildResource childResource = new ChildResource(childService);
        this.restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
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
    public static Child createEntity(EntityManager em) {
        Child child = new Child()
            .registerNumber(DEFAULT_REGISTER_NUMBER)
            .name(DEFAULT_NAME)
            .comment(DEFAULT_COMMENT);
        return child;
    }

    @Before
    public void initTest() {
        childSearchRepository.deleteAll();
        child = createEntity(em);
    }

    @Test
    @Transactional
    public void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getRegisterNumber()).isEqualTo(DEFAULT_REGISTER_NUMBER);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the Child in Elasticsearch
        Child childEs = childSearchRepository.findOne(testChild.getId());
        assertThat(childEs).isEqualToComparingFieldByField(testChild);
    }

    @Test
    @Transactional
    public void createChildWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child with an existing ID
        child.setId(1L);
        ChildDTO childDTO = childMapper.toDto(child);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRegisterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setRegisterNumber(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setName(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc.perform(post("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList
        restChildMockMvc.perform(get("/api/children?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].registerNumber").value(hasItem(DEFAULT_REGISTER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.registerNumber").value(DEFAULT_REGISTER_NUMBER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get("/api/children/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);
        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = childRepository.findOne(child.getId());
        updatedChild
            .registerNumber(UPDATED_REGISTER_NUMBER)
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        restChildMockMvc.perform(put("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getRegisterNumber()).isEqualTo(UPDATED_REGISTER_NUMBER);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the Child in Elasticsearch
        Child childEs = childSearchRepository.findOne(testChild.getId());
        assertThat(childEs).isEqualToComparingFieldByField(testChild);
    }

    @Test
    @Transactional
    public void updateNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChildMockMvc.perform(put("/api/children")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);
        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Get the child
        restChildMockMvc.perform(delete("/api/children/{id}", child.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean childExistsInEs = childSearchRepository.exists(child.getId());
        assertThat(childExistsInEs).isFalse();

        // Validate the database is empty
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);
        childSearchRepository.save(child);

        // Search the child
        restChildMockMvc.perform(get("/api/_search/children?query=id:" + child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].registerNumber").value(hasItem(DEFAULT_REGISTER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Child.class);
        Child child1 = new Child();
        child1.setId(1L);
        Child child2 = new Child();
        child2.setId(child1.getId());
        assertThat(child1).isEqualTo(child2);
        child2.setId(2L);
        assertThat(child1).isNotEqualTo(child2);
        child1.setId(null);
        assertThat(child1).isNotEqualTo(child2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChildDTO.class);
        ChildDTO childDTO1 = new ChildDTO();
        childDTO1.setId(1L);
        ChildDTO childDTO2 = new ChildDTO();
        assertThat(childDTO1).isNotEqualTo(childDTO2);
        childDTO2.setId(childDTO1.getId());
        assertThat(childDTO1).isEqualTo(childDTO2);
        childDTO2.setId(2L);
        assertThat(childDTO1).isNotEqualTo(childDTO2);
        childDTO1.setId(null);
        assertThat(childDTO1).isNotEqualTo(childDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(childMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(childMapper.fromId(null)).isNull();
    }
}
