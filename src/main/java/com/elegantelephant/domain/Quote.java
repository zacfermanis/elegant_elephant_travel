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

import com.elegantelephant.domain.enumeration.QuoteStatus;

import com.elegantelephant.domain.enumeration.VacationType;

/**
 * A Quote.
 */
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "destination")
    private String destination;

    @Column(name = "destination_airport")
    private String destinationAirport;

    @Column(name = "departure_date")
    private ZonedDateTime departureDate;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuoteStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VacationType type;

    @OneToMany(mappedBy = "quote")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne
    private Vacation vacation;

    @ManyToOne
    private QuoteRequest quoteRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Quote name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Quote description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public Quote price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public Quote destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public Quote destinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
        return this;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public Quote departureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public Quote returnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public Quote status(QuoteStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    public VacationType getType() {
        return type;
    }

    public Quote type(VacationType type) {
        this.type = type;
        return this;
    }

    public void setType(VacationType type) {
        this.type = type;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Quote passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Quote addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setQuote(this);
        return this;
    }

    public Quote removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        passenger.setQuote(null);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Vacation getVacation() {
        return vacation;
    }

    public Quote vacation(Vacation vacation) {
        this.vacation = vacation;
        return this;
    }

    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
    }

    public QuoteRequest getQuoteRequest() {
        return quoteRequest;
    }

    public Quote quoteRequest(QuoteRequest quoteRequest) {
        this.quoteRequest = quoteRequest;
        return this;
    }

    public void setQuoteRequest(QuoteRequest quoteRequest) {
        this.quoteRequest = quoteRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quote quote = (Quote) o;
        if(quote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Quote{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", price='" + price + "'" +
            ", destination='" + destination + "'" +
            ", destinationAirport='" + destinationAirport + "'" +
            ", departureDate='" + departureDate + "'" +
            ", returnDate='" + returnDate + "'" +
            ", status='" + status + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
