package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.Quote;
import com.elegantelephant.repository.QuoteRepository;
import com.elegantelephant.service.QuoteService;

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

import com.elegantelephant.domain.enumeration.QuoteStatus;
import com.elegantelephant.domain.enumeration.VacationType;
/**
 * Test class for the QuoteResource REST controller.
 *
 * @see QuoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class QuoteResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

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

    private static final QuoteStatus DEFAULT_STATUS = QuoteStatus.PENDING;
    private static final QuoteStatus UPDATED_STATUS = QuoteStatus.QUOTED;

    private static final VacationType DEFAULT_TYPE = VacationType.RESORT;
    private static final VacationType UPDATED_TYPE = VacationType.CRUISE;

    @Inject
    private QuoteRepository quoteRepository;

    @Inject
    private QuoteService quoteService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQuoteMockMvc;

    private Quote quote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuoteResource quoteResource = new QuoteResource();
        ReflectionTestUtils.setField(quoteResource, "quoteService", quoteService);
        this.restQuoteMockMvc = MockMvcBuilders.standaloneSetup(quoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity(EntityManager em) {
        Quote quote = new Quote()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .destination(DEFAULT_DESTINATION)
                .destinationAirport(DEFAULT_DESTINATION_AIRPORT)
                .departureDate(DEFAULT_DEPARTURE_DATE)
                .returnDate(DEFAULT_RETURN_DATE)
                .status(DEFAULT_STATUS)
                .type(DEFAULT_TYPE);
        return quote;
    }

    @Before
    public void initTest() {
        quote = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuote() throws Exception {
        int databaseSizeBeforeCreate = quoteRepository.findAll().size();

        // Create the Quote

        restQuoteMockMvc.perform(post("/api/quotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(quote)))
                .andExpect(status().isCreated());

        // Validate the Quote in the database
        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes).hasSize(databaseSizeBeforeCreate + 1);
        Quote testQuote = quotes.get(quotes.size() - 1);
        assertThat(testQuote.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuote.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testQuote.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testQuote.getDestinationAirport()).isEqualTo(DEFAULT_DESTINATION_AIRPORT);
        assertThat(testQuote.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testQuote.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testQuote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuote.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotes() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get all the quotes
        restQuoteMockMvc.perform(get("/api/quotes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
                .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
                .andExpect(jsonPath("$.[*].destinationAirport").value(hasItem(DEFAULT_DESTINATION_AIRPORT.toString())))
                .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE_STR)))
                .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.destinationAirport").value(DEFAULT_DESTINATION_AIRPORT.toString()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE_STR))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuote() throws Exception {
        // Initialize the database
        quoteService.save(quote);

        int databaseSizeBeforeUpdate = quoteRepository.findAll().size();

        // Update the quote
        Quote updatedQuote = quoteRepository.findOne(quote.getId());
        updatedQuote
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .price(UPDATED_PRICE)
                .destination(UPDATED_DESTINATION)
                .destinationAirport(UPDATED_DESTINATION_AIRPORT)
                .departureDate(UPDATED_DEPARTURE_DATE)
                .returnDate(UPDATED_RETURN_DATE)
                .status(UPDATED_STATUS)
                .type(UPDATED_TYPE);

        restQuoteMockMvc.perform(put("/api/quotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedQuote)))
                .andExpect(status().isOk());

        // Validate the Quote in the database
        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes).hasSize(databaseSizeBeforeUpdate);
        Quote testQuote = quotes.get(quotes.size() - 1);
        assertThat(testQuote.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuote.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testQuote.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testQuote.getDestinationAirport()).isEqualTo(UPDATED_DESTINATION_AIRPORT);
        assertThat(testQuote.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testQuote.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testQuote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuote.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteQuote() throws Exception {
        // Initialize the database
        quoteService.save(quote);

        int databaseSizeBeforeDelete = quoteRepository.findAll().size();

        // Get the quote
        restQuoteMockMvc.perform(delete("/api/quotes/{id}", quote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
