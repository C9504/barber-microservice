package org.negrdo.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.negrdo.entities.Customer;

import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, UUID> {

}
