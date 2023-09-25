package org.negrdo.resources;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.negrdo.entities.Barber;
import org.negrdo.entities.Service;
import org.negrdo.repositories.ServiceRepository;

import java.util.List;
import java.util.UUID;

@Path("/services")
public class ServiceResource {

    @Inject
    ServiceRepository serviceRepository;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Service> index() {
        List<Service> services = serviceRepository.listAll(Sort.by("createdAt").ascending());
        return services;
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Service create(Service service) {
        Barber barber = Barber.findById(service.getBarber().getId());
        service.setId(UUID.randomUUID());
        service.setBarber(barber);
        service.persistAndFlush();
        return service;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Service get(@PathParam("id") UUID id) {
        Service service = serviceRepository.findById(id);
        return service;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Service update(@PathParam("id") UUID id, Service service) {
        Service entity = Service.findById(id);
        if (entity != null) {
            entity.setName(service.getName());
            entity.setDescription(service.getDescription());
            entity.setTime(service.getTime());
            entity.setPrice(service.getPrice());
            entity.setCategory(service.getCategory());
            entity.setBarber(entity.getBarber());
            entity.setState(service.getState());
            entity.persist();
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
        Service entity = Service.findById(id);
        if (entity != null) {
            entity.delete();
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
