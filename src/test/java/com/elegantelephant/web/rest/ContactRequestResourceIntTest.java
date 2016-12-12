package com.elegantelephant.web.rest;

import com.elegantelephant.ElegantelephantApp;

import com.elegantelephant.domain.ContactRequest;
import com.elegantelephant.repository.ContactRequestRepository;
import com.elegantelephant.service.ContactRequestService;

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

/**
 * Test class for the ContactRequestResource REST controller.
 *
 * @see ContactRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElegantelephantApp.class)
public class ContactRequestResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBB";

    private static final String DEFAULT_PREFERRED_CONTACT_METHOD = "AAAAA";
    private static final String UPDATED_PREFERRED_CONTACT_METHOD = "BBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_REQUEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_REQUEST_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_REQUEST_DATE);

    @Inject
    private ContactRequestRepository contactRequestRepository;

    @Inject
    private ContactRequestService contactRequestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContactRequestMockMvc;

    private ContactRequest contactRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactRequestResource contactRequestResource = new ContactRequestResource();
        ReflectionTestUtils.setField(contactRequestResource, "contactRequestService", contactRequestService);
        this.restContactRequestMockMvc = MockMvcBuilders.standaloneSetup(contactRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRequest createEntity(EntityManager em) {
        ContactRequest contactRequest = new ContactRequest()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .contactPhone(DEFAULT_CONTACT_PHONE)
                .preferredContactMethod(DEFAULT_PREFERRED_CONTACT_METHOD)
                .requestDate(DEFAULT_REQUEST_DATE);
        return contactRequest;
    }

    @Before
    public void initTest() {
        contactRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactRequest() throws Exception {
        int databaseSizeBeforeCreate = contactRequestRepository.findAll().size();

        // Create the ContactRequest

        restContactRequestMockMvc.perform(post("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
                .andExpect(status().isCreated());

        // Validate the ContactRequest in the database
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeCreate + 1);
        ContactRequest testContactRequest = contactRequests.get(contactRequests.size() - 1);
        assertThat(testContactRequest.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContactRequest.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContactRequest.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactRequest.getContactPhone()).isEqualTo(DEFAULT_CONTACT_PHONE);
        assertThat(testContactRequest.getPreferredContactMethod()).isEqualTo(DEFAULT_PREFERRED_CONTACT_METHOD);
        assertThat(testContactRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRequestRepository.findAll().size();
        // set the field null
        contactRequest.setFirstName(null);

        // Create the ContactRequest, which fails.

        restContactRequestMockMvc.perform(post("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
                .andExpect(status().isBadRequest());

        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRequestRepository.findAll().size();
        // set the field null
        contactRequest.setLastName(null);

        // Create the ContactRequest, which fails.

        restContactRequestMockMvc.perform(post("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
                .andExpect(status().isBadRequest());

        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRequestRepository.findAll().size();
        // set the field null
        contactRequest.setEmail(null);

        // Create the ContactRequest, which fails.

        restContactRequestMockMvc.perform(post("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
                .andExpect(status().isBadRequest());

        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreferredContactMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRequestRepository.findAll().size();
        // set the field null
        contactRequest.setPreferredContactMethod(null);

        // Create the ContactRequest, which fails.

        restContactRequestMockMvc.perform(post("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
                .andExpect(status().isBadRequest());

        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactRequests() throws Exception {
        // Initialize the database
        contactRequestRepository.saveAndFlush(contactRequest);

        // Get all the contactRequests
        restContactRequestMockMvc.perform(get("/api/contact-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contactRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].preferredContactMethod").value(hasItem(DEFAULT_PREFERRED_CONTACT_METHOD.toString())))
                .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE_STR)));
    }

    @Test
    @Transactional
    public void getContactRequest() throws Exception {
        // Initialize the database
        contactRequestRepository.saveAndFlush(contactRequest);

        // Get the contactRequest
        restContactRequestMockMvc.perform(get("/api/contact-requests/{id}", contactRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactRequest.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE.toString()))
            .andExpect(jsonPath("$.preferredContactMethod").value(DEFAULT_PREFERRED_CONTACT_METHOD.toString()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingContactRequest() throws Exception {
        // Get the contactRequest
        restContactRequestMockMvc.perform(get("/api/contact-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactRequest() throws Exception {
        // Initialize the database
        contactRequestService.save(contactRequest);

        int databaseSizeBeforeUpdate = contactRequestRepository.findAll().size();

        // Update the contactRequest
        ContactRequest updatedContactRequest = contactRequestRepository.findOne(contactRequest.getId());
        updatedContactRequest
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .email(UPDATED_EMAIL)
                .contactPhone(UPDATED_CONTACT_PHONE)
                .preferredContactMethod(UPDATED_PREFERRED_CONTACT_METHOD)
                .requestDate(UPDATED_REQUEST_DATE);

        restContactRequestMockMvc.perform(put("/api/contact-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContactRequest)))
                .andExpect(status().isOk());

        // Validate the ContactRequest in the database
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeUpdate);
        ContactRequest testContactRequest = contactRequests.get(contactRequests.size() - 1);
        assertThat(testContactRequest.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContactRequest.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContactRequest.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactRequest.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testContactRequest.getPreferredContactMethod()).isEqualTo(UPDATED_PREFERRED_CONTACT_METHOD);
        assertThat(testContactRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void deleteContactRequest() throws Exception {
        // Initialize the database
        contactRequestService.save(contactRequest);

        int databaseSizeBeforeDelete = contactRequestRepository.findAll().size();

        // Get the contactRequest
        restContactRequestMockMvc.perform(delete("/api/contact-requests/{id}", contactRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
