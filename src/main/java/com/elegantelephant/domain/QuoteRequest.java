package com.elegantelephant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.elegantelephant.domain.enumeration.VacationType;

/**
 * A QuoteRequest.
 */
@Entity
@Table(name = "quote_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuoteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "maximum_budget")
    private Integer maximumBudget;

    @Column(name = "destination")
    private String destination;

    @Column(name = "destination_airport")
    private String destinationAirport;

    @Column(name = "departure_date")
    private ZonedDateTime departureDate;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VacationType type;

    @OneToMany(mappedBy = "quoteRequest")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Passenger> passengers = new HashSet<>();

    @OneToMany(mappedBy = "quoteRequest")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Quote> quotes = new HashSet<>();

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public QuoteRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public QuoteRequest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaximumBudget() {
        return maximumBudget;
    }

    public QuoteRequest maximumBudget(Integer maximumBudget) {
        this.maximumBudget = maximumBudget;
        return this;
    }

    public void setMaximumBudget(Integer maximumBudget) {
        this.maximumBudget = maximumBudget;
    }

    public String getDestination() {
        return destination;
    }

    public QuoteRequest destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public QuoteRequest destinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
        return this;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public QuoteRequest departureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public QuoteRequest returnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public VacationType getType() {
        return type;
    }

    public QuoteRequest type(VacationType type) {
        this.type = type;
        return this;
    }

    public void setType(VacationType type) {
        this.type = type;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public QuoteRequest passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public QuoteRequest addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setQuoteRequest(this);
        return this;
    }

    public QuoteRequest removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        passenger.setQuoteRequest(null);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Set<Quote> getQuotes() {
        return quotes;
    }

    public QuoteRequest quotes(Set<Quote> quotes) {
        this.quotes = quotes;
        return this;
    }

    public QuoteRequest addQuote(Quote quote) {
        quotes.add(quote);
        quote.setQuoteRequest(this);
        return this;
    }

    public QuoteRequest removeQuote(Quote quote) {
        quotes.remove(quote);
        quote.setQuoteRequest(null);
        return this;
    }

    public void setQuotes(Set<Quote> quotes) {
        this.quotes = quotes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public QuoteRequest customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuoteRequest quoteRequest = (QuoteRequest) o;
        if(quoteRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, quoteRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuoteRequest{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", maximumBudget='" + maximumBudget + "'" +
            ", destination='" + destination + "'" +
            ", destinationAirport='" + destinationAirport + "'" +
            ", departureDate='" + departureDate + "'" +
            ", returnDate='" + returnDate + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
