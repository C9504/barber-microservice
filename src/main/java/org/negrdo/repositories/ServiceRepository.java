package org.negrdo.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.negrdo.entities.Service;

import java.util.UUID;

@ApplicationScoped
public class ServiceRepository implements PanacheRepositoryBase<Service, UUID> {

}
