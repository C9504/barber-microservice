package org.negrdo.resources;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.negrdo.entities.Appointment;
import org.negrdo.repositories.AppointmentRepository;

import java.util.List;
import java.util.UUID;

@Path("/appointments")
public class AppointmentResource {

    @Inject
    AppointmentRepository appointmentRepository;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Appointment> index() {
        return appointmentRepository.listAll(Sort.by("createdAt").ascending());
    }
//
//    @POST
//    @Transactional
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response create(Appointment appointment) {
//        //System.out.println(appointment);
//        Customer customer = Customer.findById(appointment.getCustomer().getId());
//        Barber barber = Barber.findById(appointment.getBarber().getId());
//        appointment.setId(UUID.randomUUID());
//        appointment.setCustomer(customer);
//        appointment.setBarber(barber);
//        appointment.setDateTimeAppointment(Instant.now().toEpochMilli());
//        appointment.persistAndFlush();
//        return Response.ok(appointment).build();
//    }
//
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Appointment get(@PathParam("id") UUID id) {
        return appointmentRepository.findByIdWithAppointment(id);
    }
//
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Appointment update(@PathParam("id") UUID id, Appointment appointment) {
//        Appointment entity = Appointment.findById(id);
//        Barber barber = entity.getBarber();
//        Customer customer = entity.getCustomer();
//        if (entity != null) {
//            entity.setBarber(barber);
//            entity.setCustomer(customer);
//            entity.setDateTimeAppointment(entity.getDateTimeAppointment());
//            entity.setState(appointment.getState());
//            entity.setUpdatedAt(Instant.now().toEpochMilli());
//            appointmentRepository.persist(entity);
//            return entity;
//        }
//        return entity;
//    }
//
//    @DELETE
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response delete(@PathParam("id") UUID id) {
//        Appointment entity = Appointment.findById(id);
//        if (entity != null) {
//            entity.delete();
//            return Response.noContent().build();
//        }
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }

}
