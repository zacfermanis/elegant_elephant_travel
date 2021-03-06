package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.QuoteRequest;
import com.elegantelephant.repository.QuoteRequestRepository;
import com.elegantelephant.service.QuoteRequestService;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elegantelephant.domain.enumeration.VacationType;
/**
 * Test class for the QuoteRequestResource REST controller.
 *
 * @see QuoteRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class QuoteRequestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_MAXIMUM_BUDGET = 1;
    private static final Integer UPDATED_MAXIMUM_BUDGET = 2;

    private static final String DEFAULT_DESTINATION = "AAAAA";
    private static final String UPDATED_DESTINATION = "BBBBB";

    private static final String DEFAULT_DESTINATION_AIRPORT = "AAAAA";
    private static final String UPDATED_DESTINATION_AIRPORT = "BBBBB";

    private static final ZonedDateTime DEFAULT_DEPARTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DEPARTURE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DEPARTURE_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DEPARTURE_DATE);

    private static final ZonedDateTime DEFAULT_RETURN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RETURN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RETURN_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_RETURN_DATE);

    private static final VacationType DEFAULT_TYPE = VacationType.RESORT;
    private static final VacationType UPDATED_TYPE = VacationType.CRUISE;

    @Inject
    private QuoteRequestRepository quoteRequestRepository;

    @Inject
    private QuoteRequestService quoteRequestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQuoteRequestMockMvc;

    private QuoteRequest quoteRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuoteRequestResource quoteRequestResource = new QuoteRequestResource();
        ReflectionTestUtils.setField(quoteRequestResource, "quoteRequestService", quoteRequestService);
        this.restQuoteRequestMockMvc = MockMvcBuilders.standaloneSetup(quoteRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteRequest createEntity(EntityManager em) {
        QuoteRequest quoteRequest = new QuoteRequest()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .maximumBudget(DEFAULT_MAXIMUM_BUDGET)
                .destination(DEFAULT_DESTINATION)
                .destinationAirport(DEFAULT_DESTINATION_AIRPORT)
                .departureDate(DEFAULT_DEPARTURE_DATE)
                .returnDate(DEFAULT_RETURN_DATE)
                .type(DEFAULT_TYPE);
        return quoteRequest;
    }

    @Before
    public void initTest() {
        quoteRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuoteRequest() throws Exception {
        int databaseSizeBeforeCreate = quoteRequestRepository.findAll().size();

        // Create the QuoteRequest

        restQuoteRequestMockMvc.perform(post("/api/quote-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(quoteRequest)))
                .andExpect(status().isCreated());

        // Validate the QuoteRequest in the database
        List<QuoteRequest> quoteRequests = quoteRequestRepository.findAll();
        assertThat(quoteRequests).hasSize(databaseSizeBeforeCreate + 1);
        QuoteRequest testQuoteRequest = quoteRequests.get(quoteRequests.size() - 1);
        assertThat(testQuoteRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuoteRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuoteRequest.getMaximumBudget()).isEqualTo(DEFAULT_MAXIMUM_BUDGET);
        assertThat(testQuoteRequest.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testQuoteRequest.getDestinationAirport()).isEqualTo(DEFAULT_DESTINATION_AIRPORT);
        assertThat(testQuoteRequest.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testQuoteRequest.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testQuoteRequest.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuoteRequests() throws Exception {
        // Initialize the database
        quoteRequestRepository.saveAndFlush(quoteRequest);

        // Get all the quoteRequests
        restQuoteRequestMockMvc.perform(get("/api/quote-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(quoteRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].maximumBudget").value(hasItem(DEFAULT_MAXIMUM_BUDGET)))
                .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
                .andExpect(jsonPath("$.[*].destinationAirport").value(hasItem(DEFAULT_DESTINATION_AIRPORT.toString())))
                .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE_STR)))
                .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE_STR)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getQuoteRequest() throws Exception {
        // Initialize the database
        quoteRequestRepository.saveAndFlush(quoteRequest);

        // Get the quoteRequest
        restQuoteRequestMockMvc.perform(get("/api/quote-requests/{id}", quoteRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quoteRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.maximumBudget").value(DEFAULT_MAXIMUM_BUDGET))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.destinationAirport").value(DEFAULT_DESTINATION_AIRPORT.toString()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE_STR))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE_STR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuoteRequest() throws Exception {
        // Get the quoteRequest
        restQuoteRequestMockMvc.perform(get("/api/quote-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuoteRequest() throws Exception {
        // Initialize the database
        quoteRequestService.save(quoteRequest);

        int databaseSizeBeforeUpdate = quoteRequestRepository.findAll().size();

        // Update the quoteRequest
        QuoteRequest updatedQuoteRequest = quoteRequestRepository.findOne(quoteRequest.getId());
        updatedQuoteRequest
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .maximumBudget(UPDATED_MAXIMUM_BUDGET)
                .destination(UPDATED_DESTINATION)
                .destinationAirport(UPDATED_DESTINATION_AIRPORT)
                .departureDate(UPDATED_DEPARTURE_DATE)
                .returnDate(UPDATED_RETURN_DATE)
                .type(UPDATED_TYPE);

        restQuoteRequestMockMvc.perform(put("/api/quote-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedQuoteRequest)))
                .andExpect(status().isOk());

        // Validate the QuoteRequest in the database
        List<QuoteRequest> quoteRequests = quoteRequestRepository.findAll();
        assertThat(quoteRequests).hasSize(databaseSizeBeforeUpdate);
        QuoteRequest testQuoteRequest = quoteRequests.get(quoteRequests.size() - 1);
        assertThat(testQuoteRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuoteRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuoteRequest.getMaximumBudget()).isEqualTo(UPDATED_MAXIMUM_BUDGET);
        assertThat(testQuoteRequest.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testQuoteRequest.getDestinationAirport()).isEqualTo(UPDATED_DESTINATION_AIRPORT);
        assertThat(testQuoteRequest.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testQuoteRequest.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testQuoteRequest.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteQuoteRequest() throws Exception {
        // Initialize the database
        quoteRequestService.save(quoteRequest);

        int databaseSizeBeforeDelete = quoteRequestRepository.findAll().size();

        // Get the quoteRequest
        restQuoteRequestMockMvc.perform(delete("/api/quote-requests/{id}", quoteRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuoteRequest> quoteRequests = quoteRequestRepository.findAll();
        assertThat(quoteRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
