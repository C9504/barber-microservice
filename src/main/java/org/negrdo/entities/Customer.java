package org.negrdo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntityBase {

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @Column(name = "subject_id", nullable = false, length = 36)
    private UUID subjectId;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "address", nullable = false, length = 100)
    private String address;
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "state", nullable = false)
    private String state = "ACTIVE";
    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().toEpochMilli();
    @Column(name = "updated_at", nullable = false)
    private Long updatedAt = Instant.now().toEpochMilli();

    @OneToMany(mappedBy = "customer")
    private Set<Appointment> appointments = new HashSet<>();

    public Customer() {

    }

    public Customer(UUID id, UUID subjectId, String name, String lastName, String address, String phone, String email, String state, Long createdAt, Long updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}