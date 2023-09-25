package org.negrdo.resources;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.negrdo.entities.Barber;
import org.negrdo.entities.Customer;
import org.negrdo.repositories.BarberRepository;

import java.util.List;
import java.util.UUID;

@Path("/barbers")
public class BarberResource {

    @Inject
    BarberRepository barberRepository;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Barber> index() {
        List<Barber> barbers = barberRepository.listAll(Sort.by("createdAt").ascending());
        return barbers;
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Barber create(Barber barber) {
        barber.setId(UUID.randomUUID());
        barber.persistAndFlush();
        return barber;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Barber get(@PathParam("id") UUID id) {
        Barber barber = barberRepository.findById(id);
        return barber;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Barber update(@PathParam("id") UUID id, Barber barber) {
        Barber entity = Barber.findById(id);
        if (entity != null) {
            entity.setName(barber.getName());
            entity.setLastName(barber.getLastName());
            entity.setAddress(barber.getAddress());
            entity.setEmail(barber.getEmail());
            entity.setPhone(barber.getPhone());
            barberRepository.persist(entity);
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
        Barber entity = Barber.findById(id);
        if (entity != null) {
            entity.delete();
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
