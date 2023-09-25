package org.negrdo.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.negrdo.entities.Barber;

import java.util.UUID;

@ApplicationScoped
public class BarberRepository implements PanacheRepositoryBase<Barber, UUID> {
}
