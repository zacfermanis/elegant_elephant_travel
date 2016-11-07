package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.Deal;
import com.elegantelephant.repository.DealRepository;
import com.elegantelephant.service.DealService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DealResource REST controller.
 *
 * @see DealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class DealResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_HEAD_LINE = "AAAAA";
    private static final String UPDATED_HEAD_LINE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_STARTING_PRICE = 1;
    private static final Integer UPDATED_STARTING_PRICE = 2;

    private static final Integer DEFAULT_HIGH_PRICE = 1;
    private static final Integer UPDATED_HIGH_PRICE = 2;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private DealRepository dealRepository;

    @Inject
    private DealService dealService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDealMockMvc;

    private Deal deal;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DealResource dealResource = new DealResource();
        ReflectionTestUtils.setField(dealResource, "dealService", dealService);
        this.restDealMockMvc = MockMvcBuilders.standaloneSetup(dealResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createEntity(EntityManager em) {
        Deal deal = new Deal()
                .name(DEFAULT_NAME)
                .headLine(DEFAULT_HEAD_LINE)
                .description(DEFAULT_DESCRIPTION)
                .startingPrice(DEFAULT_STARTING_PRICE)
                .highPrice(DEFAULT_HIGH_PRICE)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .url(DEFAULT_URL)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return deal;
    }

    @Before
    public void initTest() {
        deal = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeal() throws Exception {
        int databaseSizeBeforeCreate = dealRepository.findAll().size();

        // Create the Deal

        restDealMockMvc.perform(post("/api/deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deal)))
                .andExpect(status().isCreated());

        // Validate the Deal in the database
        List<Deal> deals = dealRepository.findAll();
        assertThat(deals).hasSize(databaseSizeBeforeCreate + 1);
        Deal testDeal = deals.get(deals.size() - 1);
        assertThat(testDeal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeal.getHeadLine()).isEqualTo(DEFAULT_HEAD_LINE);
        assertThat(testDeal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeal.getStartingPrice()).isEqualTo(DEFAULT_STARTING_PRICE);
        assertThat(testDeal.getHighPrice()).isEqualTo(DEFAULT_HIGH_PRICE);
        assertThat(testDeal.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDeal.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testDeal.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDeal.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDeal.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllDeals() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the deals
        restDealMockMvc.perform(get("/api/deals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].headLine").value(hasItem(DEFAULT_HEAD_LINE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startingPrice").value(hasItem(DEFAULT_STARTING_PRICE)))
                .andExpect(jsonPath("$.[*].highPrice").value(hasItem(DEFAULT_HIGH_PRICE)))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDeal() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.headLine").value(DEFAULT_HEAD_LINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startingPrice").value(DEFAULT_STARTING_PRICE))
            .andExpect(jsonPath("$.highPrice").value(DEFAULT_HIGH_PRICE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeal() throws Exception {
        // Initialize the database
        dealService.save(deal);

        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Update the deal
        Deal updatedDeal = dealRepository.findOne(deal.getId());
        updatedDeal
                .name(UPDATED_NAME)
                .headLine(UPDATED_HEAD_LINE)
                .description(UPDATED_DESCRIPTION)
                .startingPrice(UPDATED_STARTING_PRICE)
                .highPrice(UPDATED_HIGH_PRICE)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .url(UPDATED_URL)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restDealMockMvc.perform(put("/api/deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeal)))
                .andExpect(status().isOk());

        // Validate the Deal in the database
        List<Deal> deals = dealRepository.findAll();
        assertThat(deals).hasSize(databaseSizeBeforeUpdate);
        Deal testDeal = deals.get(deals.size() - 1);
        assertThat(testDeal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeal.getHeadLine()).isEqualTo(UPDATED_HEAD_LINE);
        assertThat(testDeal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeal.getStartingPrice()).isEqualTo(UPDATED_STARTING_PRICE);
        assertThat(testDeal.getHighPrice()).isEqualTo(UPDATED_HIGH_PRICE);
        assertThat(testDeal.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDeal.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testDeal.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDeal.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDeal.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteDeal() throws Exception {
        // Initialize the database
        dealService.save(deal);

        int databaseSizeBeforeDelete = dealRepository.findAll().size();

        // Get the deal
        restDealMockMvc.perform(delete("/api/deals/{id}", deal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Deal> deals = dealRepository.findAll();
        assertThat(deals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
