package org.negrdo.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.negrdo.entities.Appointment;

import java.util.UUID;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepositoryBase<Appointment, UUID> {

    @PersistenceContext
    EntityManager entityManager;

    public Appointment findByIdWithAppointment(UUID id) {
        return entityManager.createQuery(
                "SELECT a FROM Appointment a " +
                        "LEFT JOIN FETCH a.customer " +
                        "LEFT JOIN FETCH a.barber " +
                        "WHERE a.id = :id",
                Appointment.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
