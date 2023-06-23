package grycap.keycloak.eventlistener.emailsender;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.storage.adapter.InMemoryUserAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailSenderProvider;

public class EmailSenderEventListenerProvider implements EventListenerProvider, FormAction {

    private final KeycloakSession session;
//  private static final Logger log = Logger.getLogger(EmailSenderEventListenerProviderFactory.class);
    private static final Logger LOG = Logger.getLogger(EmailSenderEventListenerProvider.class);

    private Set<String> groups;
    private ResourceType target_event;
    private String kubeauthorizer_endpoint, /*email_subject*/kubeauthorizer_token, kubeauthorizer_userclaim;
//   private String[] email_receivers;
    private RealmProvider model;
    private Set<String> target_groups;

    public EmailSenderEventListenerProvider(KeycloakSession session, ResourceType event, Set<String> groups, String kubeauthorizer_endpoint, String kubeauthorizer_token, String kubeauthorizer_userclaim/*, String email_subject, String[] email_receivers*/)  {
        this.session = session;
        this.model = session.realms();
        this.target_event = event;
        this.target_groups = groups;
        this.kubeauthorizer_endpoint = kubeauthorizer_endpoint;
        this.kubeauthorizer_token = kubeauthorizer_token;
        this.kubeauthorizer_userclaim = kubeauthorizer_userclaim;
    //    this.email_subject = email_subject;
    //    this.email_receivers = email_receivers;
    }

    @Override
    public void close() {
        // Método requerido por la interfaz, pero que no necesitamos implementar
    }

    @Override
    public void success(FormContext context) {
        UserModel user = context.getUser();
        RealmModel realm = context.getRealm();
        
        // List of admin emails
        List<String> adminEmails = Arrays.asList("admin@example.com", "adm@ex.com");

        for (String emailAdmin : adminEmails) {
            UserModel admin = new InMemoryUserAdapter(context.getSession(), realm, UUID.randomUUID().toString());
            admin.setEmail(emailAdmin);
            LOG.info("EmailFormActionFactory success, sent email to " + emailAdmin + " mentioning that " + user.getEmail() + " has registered!" );
            EmailSenderProvider emailSender = context.getSession().getProvider(EmailSenderProvider.class);
            try {
                emailSender.send(realm.getSmtpConfig(), admin, "Self Registration with Keycloak", "Hi Admin, a new user with the email "
                                + user.getEmail() + " has just registered with keycloak! " +
                                "This is an automatic notice.",
                                "<h3>Hi Admin,</h3>" +
                                "<p>a new user with the email " + user.getEmail() + " has just registered with keycloak! </p>" +
                                "<p>This is an automatic notice." );
            } catch (EmailException e) {
                LOG.error("EmailFormActionFactory success, could not send notification to admin", e);
            }
        }
    }


    @Override
    public void buildPage(FormContext context, LoginFormsProvider form) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildPage'");
    }


    @Override
    public void validate(ValidationContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }


    @Override
    public boolean requiresUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requiresUser'");
    }


    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'configuredFor'");
    }


    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRequiredActions'");
    }


    @Override
    public void onEvent(Event event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
    }


    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
    }
}
