package grycap.keycloak.eventlistener.emailsender;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class EmailSenderEventListenerProviderFactory implements EventListenerProviderFactory {

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new EmailSenderEventListenerProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        // Método requerido por la interfaz, pero que no necesitamos implementar
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Método requerido por la interfaz, pero que no necesitamos implementar
    }

    @Override
    public void close() {
        // Método requerido por la interfaz, pero que no necesitamos implementar
    }

    @Override
    public String getId() {
        return "emailSenderEventListener";
        /**
         * The getId() method is used to provide a unique identifier for the custom event listener provider factory.
         * This identifier is used within Keycloak to reference and lookup the correct factory when instances of the
         * EventListenerProvider interface are needed.
         * When an event occurs within Keycloak that needs to be broadcasted to listeners, Keycloak looks up the 
         * appropriate listener based on the ID returned by getId()
         */
    }
}