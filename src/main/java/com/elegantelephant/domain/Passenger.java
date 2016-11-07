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
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "ssn")
    private String ssn;

    @ManyToOne
    private QuoteRequest quoteRequest;

    @ManyToOne
    private Quote quote;

    @ManyToMany(mappedBy = "passengers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vacation> vacations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Passenger firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Passenger lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Passenger dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public Passenger ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public QuoteRequest getQuoteRequest() {
        return quoteRequest;
    }

    public Passenger quoteRequest(QuoteRequest quoteRequest) {
        this.quoteRequest = quoteRequest;
        return this;
    }

    public void setQuoteRequest(QuoteRequest quoteRequest) {
        this.quoteRequest = quoteRequest;
    }

    public Quote getQuote() {
        return quote;
    }

    public Passenger quote(Quote quote) {
        this.quote = quote;
        return this;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public Passenger vacations(Set<Vacation> vacations) {
        this.vacations = vacations;
        return this;
    }

    public Passenger addVacation(Vacation vacation) {
        vacations.add(vacation);
        vacation.getPassengers().add(this);
        return this;
    }

    public Passenger removeVacation(Vacation vacation) {
        vacations.remove(vacation);
        vacation.getPassengers().remove(this);
        return this;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Passenger passenger = (Passenger) o;
        if(passenger.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, passenger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", dateOfBirth='" + dateOfBirth + "'" +
            ", ssn='" + ssn + "'" +
            '}';
    }
}
