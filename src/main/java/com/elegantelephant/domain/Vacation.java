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

import com.elegantelephant.domain.enumeration.VacationStatus;

import com.elegantelephant.domain.enumeration.VacationType;

/**
 * A Vacation.
 */
@Entity
@Table(name = "vacation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vacation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "destination")
    private String destination;

    @Column(name = "destination_airport")
    private String destinationAirport;

    @Column(name = "departure_date")
    private ZonedDateTime departureDate;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Lob
    @Column(name = "signature")
    private byte[] signature;

    @Column(name = "signature_content_type")
    private String signatureContentType;

    @Column(name = "travel_protection")
    private Boolean travelProtection;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VacationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VacationType type;

    @OneToOne
    @JoinColumn(unique = true)
    private Card card;

    @OneToMany(mappedBy = "vacation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Quote> quotes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "vacation_passenger",
               joinColumns = @JoinColumn(name="vacations_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="passengers_id", referencedColumnName="ID"))
    private Set<Passenger> passengers = new HashSet<>();

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

    public Vacation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public Vacation price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Vacation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public Vacation destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public Vacation destinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
        return this;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public Vacation departureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public Vacation returnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Vacation signature(byte[] signature) {
        this.signature = signature;
        return this;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public Vacation signatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
        return this;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public Boolean isTravelProtection() {
        return travelProtection;
    }

    public Vacation travelProtection(Boolean travelProtection) {
        this.travelProtection = travelProtection;
        return this;
    }

    public void setTravelProtection(Boolean travelProtection) {
        this.travelProtection = travelProtection;
    }

    public VacationStatus getStatus() {
        return status;
    }

    public Vacation status(VacationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(VacationStatus status) {
        this.status = status;
    }

    public VacationType getType() {
        return type;
    }

    public Vacation type(VacationType type) {
        this.type = type;
        return this;
    }

    public void setType(VacationType type) {
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public Vacation card(Card card) {
        this.card = card;
        return this;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Set<Quote> getQuotes() {
        return quotes;
    }

    public Vacation quotes(Set<Quote> quotes) {
        this.quotes = quotes;
        return this;
    }

    public Vacation addQuote(Quote quote) {
        quotes.add(quote);
        quote.setVacation(this);
        return this;
    }

    public Vacation removeQuote(Quote quote) {
        quotes.remove(quote);
        quote.setVacation(null);
        return this;
    }

    public void setQuotes(Set<Quote> quotes) {
        this.quotes = quotes;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Vacation passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Vacation addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.getVacations().add(this);
        return this;
    }

    public Vacation removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        passenger.getVacations().remove(this);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vacation customer(Customer customer) {
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
        Vacation vacation = (Vacation) o;
        if(vacation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vacation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vacation{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", description='" + description + "'" +
            ", destination='" + destination + "'" +
            ", destinationAirport='" + destinationAirport + "'" +
            ", departureDate='" + departureDate + "'" +
            ", returnDate='" + returnDate + "'" +
            ", signature='" + signature + "'" +
            ", signatureContentType='" + signatureContentType + "'" +
            ", travelProtection='" + travelProtection + "'" +
            ", status='" + status + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
