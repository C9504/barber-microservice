package org.negrdo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment extends PanacheEntityBase {

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;
    @Column(name = "date_time_appointment", nullable = false)
    private Long dateTimeAppointment;
    @Column(name = "state", nullable = false)
    private String state = "PENDING";
    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().toEpochMilli();
    @Column(name = "updated_at", nullable = false)
    private Long updatedAt = Instant.now().toEpochMilli();

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonIgnore
    @JsonBackReference("customerPreference")
    private Customer customer;
    @ManyToOne(targetEntity = Barber.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference("barberPreference")
    private Barber barber;

    public Appointment() {
    }

    public Appointment(UUID id, Long dateTimeAppointment, String state, Long createdAt, Long updatedAt) {
        this.id = id;
        this.dateTimeAppointment = dateTimeAppointment;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        return id != null && id.equals(((Appointment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getDateTimeAppointment() {
        return dateTimeAppointment;
    }

    public void setDateTimeAppointment(Long dateTimeAppointment) {
        this.dateTimeAppointment = dateTimeAppointment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }
}
