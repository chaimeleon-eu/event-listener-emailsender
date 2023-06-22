package grycap.keycloak.eventlistener.emailsender;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailException;

public class EmailSenderEventListenerProvider implements EventListenerProvider {

    private final KeycloakSession session;

    public EmailSenderEventListenerProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.REGISTER) {
            
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(realm, event.getUserId());

            String emailPlainContent = "New user registration\n\n" +
                "Email: " + newRegisteredUser.getEmail() + "\n" +
                "Username: " + newRegisteredUser.getUsername() + "\n" +
                "Client: " + event.getClientId();

            String emailHtmlContent = "<h1>New user registration</h1>" +
                "<ul>" +
                "<li>Email: " + newRegisteredUser.getEmail() + "</li>" +
                "<li>Username: " + newRegisteredUser.getUsername() + "</li>" +
                "<li>Client: " + event.getClientId() + "</li>" +
                "</ul>";

            DefaultEmailSenderProvider senderProvider = new DefaultEmailSenderProvider(session);

            try {
                senderProvider.send(session.getContext().getRealm().getSmtpConfig(), "admin@example.com", "Keycloak - New Registration", emailPlainContent, emailHtmlContent);
            } catch (EmailException e) {
                log.error("Failed to send email", e);
            }
            
        }
    }

    @Override
    public void close() {
        // Método requerido por la interfaz, pero que no necesitamos implementar
    }
}
