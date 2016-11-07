package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.Passenger;
import com.elegantelephant.repository.PassengerRepository;
import com.elegantelephant.service.PassengerService;

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
 * Test class for the PassengerResource REST controller.
 *
 * @see PassengerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class PassengerResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SSN = "AAAAA";
    private static final String UPDATED_SSN = "BBBBB";

    @Inject
    private PassengerRepository passengerRepository;

    @Inject
    private PassengerService passengerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PassengerResource passengerResource = new PassengerResource();
        ReflectionTestUtils.setField(passengerResource, "passengerService", passengerService);
        this.restPassengerMockMvc = MockMvcBuilders.standaloneSetup(passengerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
                .ssn(DEFAULT_SSN);
        return passenger;
    }

    @Before
    public void initTest() {
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger

        restPassengerMockMvc.perform(post("/api/passengers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passenger)))
                .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengers.get(passengers.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPassenger.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testPassenger.getSsn()).isEqualTo(DEFAULT_SSN);
    }

    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengers
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
                .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN.toString())));
    }

    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerService.save(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findOne(passenger.getId());
        updatedPassenger
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .dateOfBirth(UPDATED_DATE_OF_BIRTH)
                .ssn(UPDATED_SSN);

        restPassengerMockMvc.perform(put("/api/passengers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPassenger)))
                .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengers.get(passengers.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPassenger.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPassenger.getSsn()).isEqualTo(UPDATED_SSN);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerService.save(passenger);

        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Get the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
