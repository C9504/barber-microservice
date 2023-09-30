package org.negrdo.services;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.negrdo.helpers.HttpClientSingleton;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class IdentityProviderService {

    private static final Logger logger = Logger.getLogger(IdentityProviderService.class);

    @ConfigProperty(name = "negrdo.identity-provider.url", defaultValue = "https://account.idtolu.net")
    String IDP_ADMIN_HOSTNAME;

    @ConfigProperty(name = "negrdo.identity-provider.realm")
    String IDP_ADMIN_REALM;

    @ConfigProperty(name = "negrdo.identity-provider.client")
    String IDP_ADMIN_CLIENT;

    @ConfigProperty(name = "negrdo.identity-provider.secret")
    String IDP_ADMIN_CLIENT_SECRET;

    @ConfigProperty(name = "negrdo.identity-provider.username")
    String IDP_ADMIN_USERNAME;

    @ConfigProperty(name = "negrdo.identity-provider.password")
    String IDP_ADMIN_PASSWORD;

    public String getToken() {
        HttpResponse<String> response = null;
        String urlEncoded = "";
        HttpClient httpClient = null;
        try {
            httpClient = HttpClientSingleton.getInstance();
            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "password");
            map.put("username", IDP_ADMIN_USERNAME);
            map.put("password", IDP_ADMIN_PASSWORD);
            map.put("client_id", IDP_ADMIN_CLIENT);
            map.put("client_secret", IDP_ADMIN_CLIENT_SECRET);
            urlEncoded = map.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(urlEncoded))
                    .uri(new URI(IDP_ADMIN_HOSTNAME + "/realms/" + IDP_ADMIN_REALM + "/protocol/openid-connect/token"))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject body = new JsonObject(response.body());
                return body.getString("access_token");
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public String createUser(String token, JsonObject toRegister) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        HttpClient httpClient = null;
        toRegister.remove("confirmPassword");
        toRegister.remove("role");
        toRegister.put("enabled", true);
        toRegister.put("emailVerified", true);
        toRegister.put("credentials", new JsonArray().add(
                new JsonObject().put("type", "password").put("temporary", false).put("value", toRegister.getString("password"))
        ));
        toRegister.remove("password");
        toRegister.remove("phone");
        toRegister.remove("address");
        try {
            httpClient = HttpClientSingleton.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .headers("Authorization","Bearer " + getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(toRegister.toString()))
                    .uri(new URI(IDP_ADMIN_HOSTNAME + "/admin/realms/" + IDP_ADMIN_REALM + "/users"))
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse.body());
            if (httpResponse.statusCode() == 201) {
                String headers = httpResponse.headers().firstValue("Location").get();
                response = extractUserIdFromLocation(headers);
            }
            return response;
        } catch (URISyntaxException | IOException e) {
            logger.error("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return response;
    }

    public HttpResponse<String> assignRoleToUser(String token, String userId, String clientId, String roleId, String roleName) {
        HttpResponse<String> httpResponse = null;
        HttpClient httpClient = null;
        JsonArray body = new JsonArray();
        JsonObject json = new JsonObject();
        json.put("id", roleId);
        json.put("clientRole", true);
        json.put("composite", false);
        json.put("name", roleName);
        body.add(json);
        try {
            httpClient = HttpClientSingleton.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .headers("Authorization","Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .uri(new URI(IDP_ADMIN_HOSTNAME + "/admin/realms/" + IDP_ADMIN_REALM + "/users/" + userId + "/role-mappings/clients/" + clientId))
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse.body());
            return httpResponse;
        } catch (URISyntaxException | IOException e) {
            logger.error("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return httpResponse;
    }

    public HttpResponse<String> assignExternalRoleToUser(String token, String userId, String clientId, String roleId, String roleName) {
        HttpResponse<String> httpResponse = null;
        HttpClient httpClient = null;
        JsonArray body = new JsonArray();
        JsonObject json = new JsonObject();
        json.put("id", roleId);
        json.put("clientRole", true);
        json.put("composite", false);
        json.put("name", roleName);
        body.add(json);
        try {
            httpClient = HttpClientSingleton.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .headers("Authorization","Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .uri(new URI(IDP_ADMIN_HOSTNAME + "/admin/realms/" + IDP_ADMIN_REALM + "/users/" + userId + "/role-mappings/clients/" + clientId))
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse.body());
            return httpResponse;
        } catch (URISyntaxException | IOException e) {
            logger.error("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return httpResponse;
    }

    private String extractUserIdFromLocation(String locationHeader) {
        // Extrae el último segmento de la URL como el ID del usuario
        URI uri = URI.create(locationHeader);
        String path = uri.getPath();
        String[] segments = path.split("/");
        return segments[segments.length - 1];
    }
}
