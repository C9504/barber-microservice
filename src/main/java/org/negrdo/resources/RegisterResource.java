package org.negrdo.resources;

import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.negrdo.entities.Barber;
import org.negrdo.entities.Customer;
import org.negrdo.repositories.BarberRepository;
import org.negrdo.repositories.CustomerRepository;
import org.negrdo.services.IdentityProviderService;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Path("/register")
public class RegisterResource {

    private static final Logger logger = Logger.getLogger(RegisterResource.class);
    private final AtomicLong counter = new AtomicLong(0);

    @Inject
    IdentityProviderService identityProviderService;

    @ConfigProperty(name = "negrdo.identity-provider-internal.client.id")
    String INTERNAL_CLIENT_ID;

    @ConfigProperty(name = "negrdo.identity-provider-internal.client-role-id.customer")
    String INTERNAL_CLIENT_ROLE_CUSTOMER_ID;

    @ConfigProperty(name = "negrdo.identity-provider-internal.client-role-name.customer")
    String INTERNAL_CLIENT_ROLE_CUSTOMER_NAME;

    @ConfigProperty(name = "negrdo.identity-provider-internal.client-role-id.barber")
    String INTERNAL_CLIENT_ROLE_BARBER_ID;

    @ConfigProperty(name = "negrdo.identity-provider-internal.client-role-name.barber")
    String INTERNAL_CLIENT_ROLE_BARBER_NAME;

    @ConfigProperty(name = "negrdo.identity-provider.client.id")
    String EXTERNAL_CLIENT_ID;

    @ConfigProperty(name = "negrdo.identity-provider.client-role-id.customer")
    String EXTERNAL_CLIENT_ROLE_CUSTOMER_ID;

    @ConfigProperty(name = "negrdo.identity-provider.client-role-name.customer")
    String EXTERNAL_CLIENT_ROLE_CUSTOMER_NAME;

    @ConfigProperty(name = "negrdo.identity-provider.client-role-id.barber")
    String EXTERNAL_CLIENT_ROLE_BARBER_ID;

    @ConfigProperty(name = "negrdo.identity-provider.client-role-name.barber")
    String EXTERNAL_CLIENT_ROLE_BARBER_NAME;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(JsonObject jsonObject) {
        long started = System.currentTimeMillis();
        String token = identityProviderService.getToken();
        String role = jsonObject.getString("role").trim();
        String phone = jsonObject.getString("phone").trim();
        String address = jsonObject.getString("address").trim();
        String subjectId = identityProviderService.createUser(token, jsonObject);
        if (role.trim().equalsIgnoreCase("customer") && subjectId != null) {
            identityProviderService.assignRoleToUser(token, subjectId, INTERNAL_CLIENT_ID, INTERNAL_CLIENT_ROLE_CUSTOMER_ID, INTERNAL_CLIENT_ROLE_CUSTOMER_NAME);
            identityProviderService.assignExternalRoleToUser(token, subjectId, EXTERNAL_CLIENT_ID, EXTERNAL_CLIENT_ROLE_CUSTOMER_ID, EXTERNAL_CLIENT_ROLE_CUSTOMER_NAME);
            Customer customer = new Customer();
            customer.setId(UUID.randomUUID());
            customer.setSubjectId(UUID.fromString(subjectId));
            customer.setName(jsonObject.getString("firstName").trim());
            customer.setLastName(jsonObject.getString("lastName").trim());
            customer.setAddress(address);
            customer.setEmail(jsonObject.getString("email").trim());
            customer.setPhone(phone);
            customer.setState("ACTIVE");
            customer.persistAndFlush();
            final Long invocationNumber = counter.getAndIncrement();
            logger.infof("RegisterResource#create():customer invocation #%d returning successfully | #%d timed out after %d ms", invocationNumber, invocationNumber, System.currentTimeMillis() - started);
            return Response.ok().entity(customer).build();
        } else if (role.trim().equalsIgnoreCase("barber") && subjectId != null){
            identityProviderService.assignRoleToUser(token, subjectId, INTERNAL_CLIENT_ID, INTERNAL_CLIENT_ROLE_BARBER_ID, INTERNAL_CLIENT_ROLE_BARBER_NAME);
            identityProviderService.assignExternalRoleToUser(token, subjectId, EXTERNAL_CLIENT_ID, EXTERNAL_CLIENT_ROLE_BARBER_ID, EXTERNAL_CLIENT_ROLE_BARBER_NAME);
            Barber barber = new Barber();
            barber.setId(UUID.randomUUID());
            barber.setSubjectId(UUID.fromString(subjectId));
            barber.setName(jsonObject.getString("firstName").trim());
            barber.setLastName(jsonObject.getString("lastName").trim());
            barber.setAddress(address);
            barber.setEmail(jsonObject.getString("email").trim());
            barber.setPhone(phone);
            barber.setState("ACTIVE");
            barber.persistAndFlush();
            final Long invocationNumber = counter.getAndIncrement();
            logger.infof("RegisterResource#create():barber invocation #%d returning successfully | #%d timed out after %d ms", invocationNumber, invocationNumber, System.currentTimeMillis() - started);
            return Response.ok().entity(barber).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}