package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.Card;
import com.elegantelephant.repository.CardRepository;
import com.elegantelephant.service.CardService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class CardResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAA";
    private static final String UPDATED_NUMBER = "BBBBB";

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CCV = "AAAAA";
    private static final String UPDATED_CCV = "BBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    @Inject
    private CardRepository cardRepository;

    @Inject
    private CardService cardService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCardMockMvc;

    private Card card;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardResource cardResource = new CardResource();
        ReflectionTestUtils.setField(cardResource, "cardService", cardService);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
                .number(DEFAULT_NUMBER)
                .type(DEFAULT_TYPE)
                .expirationDate(DEFAULT_EXPIRATION_DATE)
                .ccv(DEFAULT_CCV)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME);
        return card;
    }

    @Before
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card)))
                .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCard.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCard.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testCard.getCcv()).isEqualTo(DEFAULT_CCV);
        assertThat(testCard.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCard.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cards
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].ccv").value(hasItem(DEFAULT_CCV.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.ccv").value(DEFAULT_CCV.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findOne(card.getId());
        updatedCard
                .number(UPDATED_NUMBER)
                .type(UPDATED_TYPE)
                .expirationDate(UPDATED_EXPIRATION_DATE)
                .ccv(UPDATED_CCV)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME);

        restCardMockMvc.perform(put("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCard)))
                .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCard.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCard.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testCard.getCcv()).isEqualTo(UPDATED_CCV);
        assertThat(testCard.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCard.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeDelete - 1);
    }
}
