package org.negrdo.resources;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.negrdo.entities.Customer;
import org.negrdo.repositories.CustomerRepository;

import java.util.List;
import java.util.UUID;

@Path("/customers")
public class CustomerResource {

    @Inject
    CustomerRepository customerRepository;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> index() {
        return customerRepository.listAll(Sort.by("createdAt").ascending());
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer create(Customer customer) {
        customer.setId(UUID.randomUUID());
        customer.persistAndFlush();
        return customer;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer get(@PathParam("id") UUID id) {
        return customerRepository.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Customer update(@PathParam("id") UUID id, Customer customer) {
        Customer entity = Customer.findById(id);
        if (entity != null) {
            entity.setName(customer.getName());
            entity.setLastName(customer.getLastName());
            entity.setAddress(customer.getAddress());
            entity.setEmail(customer.getEmail());
            entity.setPhone(customer.getPhone());
            customerRepository.persist(entity);
            return entity;
        }
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        Customer entity = Customer.findById(id);
        if (entity != null) {
            entity.delete();
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
