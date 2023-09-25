package org.negrdo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service extends PanacheEntityBase {


    @Id
    @Column(unique = true, nullable = false, columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @Column(name = "name", nullable = false, length = 36)
    private String name;
    @Column(name = "description", nullable = false, length = 50)
    private String description;
    @Column(name = "time", nullable = false, length = 50)
    private String time;
    @Column(name = "price", nullable = false, length = 100)
    private Double price;
    @Column(name = "category", nullable = false, length = 15)
    private String category;
    @Column(name = "image", nullable = false, unique = true, length = 100)
    private String image;
    @Column(name = "state", nullable = false)
    private String state = "ACTIVE";
    @Column(name = "created_at", nullable = false)
    private Long createdAt = Instant.now().toEpochMilli();
    @Column(name = "updated_at", nullable = false)
    private Long updatedAt = Instant.now().toEpochMilli();

    @ManyToOne(targetEntity = Barber.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference("serviceBarberPreference")
    private Barber barber;

    public Service() {
    }

    public Service(UUID id, String name, String description, String time, Double price, String category, String image, String state, Long createdAt, Long updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.price = price;
        this.category = category;
        this.image = image;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        return id != null && id.equals(((Service) o).id);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }
}
