package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.Vacation;
import com.elegantelephant.repository.VacationRepository;
import com.elegantelephant.service.VacationService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elegantelephant.domain.enumeration.VacationStatus;
import com.elegantelephant.domain.enumeration.VacationType;
/**
 * Test class for the VacationResource REST controller.
 *
 * @see VacationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class VacationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

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

    private static final byte[] DEFAULT_SIGNATURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SIGNATURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SIGNATURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SIGNATURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_TRAVEL_PROTECTION = false;
    private static final Boolean UPDATED_TRAVEL_PROTECTION = true;

    private static final VacationStatus DEFAULT_STATUS = VacationStatus.DOWNPAYMENT;
    private static final VacationStatus UPDATED_STATUS = VacationStatus.PAID_IN_FULL;

    private static final VacationType DEFAULT_TYPE = VacationType.RESORT;
    private static final VacationType UPDATED_TYPE = VacationType.CRUISE;

    @Inject
    private VacationRepository vacationRepository;

    @Inject
    private VacationService vacationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVacationMockMvc;

    private Vacation vacation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacationResource vacationResource = new VacationResource();
        ReflectionTestUtils.setField(vacationResource, "vacationService", vacationService);
        this.restVacationMockMvc = MockMvcBuilders.standaloneSetup(vacationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacation createEntity(EntityManager em) {
        Vacation vacation = new Vacation()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .description(DEFAULT_DESCRIPTION)
                .destination(DEFAULT_DESTINATION)
                .destinationAirport(DEFAULT_DESTINATION_AIRPORT)
                .departureDate(DEFAULT_DEPARTURE_DATE)
                .returnDate(DEFAULT_RETURN_DATE)
                .signature(DEFAULT_SIGNATURE)
                .signatureContentType(DEFAULT_SIGNATURE_CONTENT_TYPE)
                .travelProtection(DEFAULT_TRAVEL_PROTECTION)
                .status(DEFAULT_STATUS)
                .type(DEFAULT_TYPE);
        return vacation;
    }

    @Before
    public void initTest() {
        vacation = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacation() throws Exception {
        int databaseSizeBeforeCreate = vacationRepository.findAll().size();

        // Create the Vacation

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacation)))
                .andExpect(status().isCreated());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeCreate + 1);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVacation.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVacation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVacation.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testVacation.getDestinationAirport()).isEqualTo(DEFAULT_DESTINATION_AIRPORT);
        assertThat(testVacation.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testVacation.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testVacation.getSignature()).isEqualTo(DEFAULT_SIGNATURE);
        assertThat(testVacation.getSignatureContentType()).isEqualTo(DEFAULT_SIGNATURE_CONTENT_TYPE);
        assertThat(testVacation.isTravelProtection()).isEqualTo(DEFAULT_TRAVEL_PROTECTION);
        assertThat(testVacation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVacation.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllVacations() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get all the vacations
        restVacationMockMvc.perform(get("/api/vacations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vacation.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
                .andExpect(jsonPath("$.[*].destinationAirport").value(hasItem(DEFAULT_DESTINATION_AIRPORT.toString())))
                .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE_STR)))
                .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE_STR)))
                .andExpect(jsonPath("$.[*].signatureContentType").value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].signature").value(hasItem(Base64Utils.encodeToString(DEFAULT_SIGNATURE))))
                .andExpect(jsonPath("$.[*].travelProtection").value(hasItem(DEFAULT_TRAVEL_PROTECTION.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", vacation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vacation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.destinationAirport").value(DEFAULT_DESTINATION_AIRPORT.toString()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE_STR))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE_STR))
            .andExpect(jsonPath("$.signatureContentType").value(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.signature").value(Base64Utils.encodeToString(DEFAULT_SIGNATURE)))
            .andExpect(jsonPath("$.travelProtection").value(DEFAULT_TRAVEL_PROTECTION.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVacation() throws Exception {
        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacation() throws Exception {
        // Initialize the database
        vacationService.save(vacation);

        int databaseSizeBeforeUpdate = vacationRepository.findAll().size();

        // Update the vacation
        Vacation updatedVacation = vacationRepository.findOne(vacation.getId());
        updatedVacation
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .description(UPDATED_DESCRIPTION)
                .destination(UPDATED_DESTINATION)
                .destinationAirport(UPDATED_DESTINATION_AIRPORT)
                .departureDate(UPDATED_DEPARTURE_DATE)
                .returnDate(UPDATED_RETURN_DATE)
                .signature(UPDATED_SIGNATURE)
                .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE)
                .travelProtection(UPDATED_TRAVEL_PROTECTION)
                .status(UPDATED_STATUS)
                .type(UPDATED_TYPE);

        restVacationMockMvc.perform(put("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVacation)))
                .andExpect(status().isOk());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeUpdate);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVacation.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVacation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVacation.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testVacation.getDestinationAirport()).isEqualTo(UPDATED_DESTINATION_AIRPORT);
        assertThat(testVacation.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testVacation.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testVacation.getSignature()).isEqualTo(UPDATED_SIGNATURE);
        assertThat(testVacation.getSignatureContentType()).isEqualTo(UPDATED_SIGNATURE_CONTENT_TYPE);
        assertThat(testVacation.isTravelProtection()).isEqualTo(UPDATED_TRAVEL_PROTECTION);
        assertThat(testVacation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVacation.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteVacation() throws Exception {
        // Initialize the database
        vacationService.save(vacation);

        int databaseSizeBeforeDelete = vacationRepository.findAll().size();

        // Get the vacation
        restVacationMockMvc.perform(delete("/api/vacations/{id}", vacation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
