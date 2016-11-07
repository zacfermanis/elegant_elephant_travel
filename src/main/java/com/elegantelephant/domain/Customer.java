package com.elegantelephant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "display_full")
    private String displayFull;

    @Column(name = "display_first")
    private String displayFirst;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(unique = true)
    private Address mailingAddress;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vacation> vacations = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuoteRequest> quoteRequests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayFull() {
        return displayFull;
    }

    public Customer displayFull(String displayFull) {
        this.displayFull = displayFull;
        return this;
    }

    public void setDisplayFull(String displayFull) {
        this.displayFull = displayFull;
    }

    public String getDisplayFirst() {
        return displayFirst;
    }

    public Customer displayFirst(String displayFirst) {
        this.displayFirst = displayFirst;
        return this;
    }

    public void setDisplayFirst(String displayFirst) {
        this.displayFirst = displayFirst;
    }

    public String getSalutation() {
        return salutation;
    }

    public Customer salutation(String salutation) {
        this.salutation = salutation;
        return this;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Customer dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public Customer mailingAddress(Address address) {
        this.mailingAddress = address;
        return this;
    }

    public void setMailingAddress(Address address) {
        this.mailingAddress = address;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public Customer vacations(Set<Vacation> vacations) {
        this.vacations = vacations;
        return this;
    }

    public Customer addVacation(Vacation vacation) {
        vacations.add(vacation);
        vacation.setCustomer(this);
        return this;
    }

    public Customer removeVacation(Vacation vacation) {
        vacations.remove(vacation);
        vacation.setCustomer(null);
        return this;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

    public Set<QuoteRequest> getQuoteRequests() {
        return quoteRequests;
    }

    public Customer quoteRequests(Set<QuoteRequest> quoteRequests) {
        this.quoteRequests = quoteRequests;
        return this;
    }

    public Customer addQuoteRequest(QuoteRequest quoteRequest) {
        quoteRequests.add(quoteRequest);
        quoteRequest.setCustomer(this);
        return this;
    }

    public Customer removeQuoteRequest(QuoteRequest quoteRequest) {
        quoteRequests.remove(quoteRequest);
        quoteRequest.setCustomer(null);
        return this;
    }

    public void setQuoteRequests(Set<QuoteRequest> quoteRequests) {
        this.quoteRequests = quoteRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if(customer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", displayFull='" + displayFull + "'" +
            ", displayFirst='" + displayFirst + "'" +
            ", salutation='" + salutation + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", dateOfBirth='" + dateOfBirth + "'" +
            '}';
    }
}
