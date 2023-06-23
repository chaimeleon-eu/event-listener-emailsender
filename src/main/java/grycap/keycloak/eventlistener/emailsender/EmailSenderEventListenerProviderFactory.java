package grycap.keycloak.eventlistener.emailsender;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;

public class EmailSenderEventListenerProviderFactory implements EventListenerProviderFactory, ServerInfoAwareProviderFactory {

    private static final Logger log = Logger.getLogger(EmailSenderEventListenerProviderFactory.class);
    private Set<String> groups;
    private ResourceType target_event;
    private String kubeauthorizer_endpoint, /*email_subject*/kubeauthorizer_token, kubeauthorizer_userclaim;
//    private String[] email_receivers;
    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new EmailSenderEventListenerProvider(session, this.target_event, this.groups, this.kubeauthorizer_endpoint, this.kubeauthorizer_token, this.kubeauthorizer_userclaim);
    }

    @Override
    public void init(Config.Scope config) {
        groups = new HashSet<>();
        log.info( String.format( "### ------------  %s.init() ------------ ###", this.getId() ) );
        
        String[] group_names = config.get("OIDCGroups","").split(",");
        if (group_names != null) {
            for (String name : group_names) {
                this.groups.add(name);
            }
        }

        // this.email_receivers = config.get("email_receivers", "").split(",");

        this.target_event = ResourceType.valueOf(config.get("adminEvent", "GROUP_MEMBERSHIP" ) );
        this.kubeauthorizer_endpoint = config.get("kubeAuthorizerEndpoint", "");
        this.kubeauthorizer_token = config.get("kubeAuthorizerToken", "");
        this.kubeauthorizer_userclaim = config.get("kubeAuthorizerUserClaim", "username");
        // this.email_subject = config.get("emailSubject", "Default Subject");
        log.info ("Configuration variables: ");
        log.info ("\tOIDC groups: " + this.groups.toString());
        log.info ("\tAdmin event target: " + this.target_event.toString());
        log.info ("\tKube-authorizer endpoint: " + this.kubeauthorizer_endpoint );
        log.info ("\tKube-authorizer token: " + this.kubeauthorizer_token);
        log.info ("\tKube-authorizer username claim [sub,username] : " + this.kubeauthorizer_userclaim );
/*         log.info ("\tEmail subject: " + this.email_subject);
        for (String email : email_receivers) {
            log.info("\tEmail receiver: " + email);
}
        
*/
        
        log.info( String.format("-----------------------------------------------------------") );
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
    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("OIDCgroups", this.groups.toString());
        ret.put("AdminEventTarget", this.target_event.toString());
        ret.put("KubeauthorizerEndpoint", this.kubeauthorizer_endpoint);
        ret.put("KubeauthorizerUserClaim", this.kubeauthorizer_userclaim);
        return ret;
    }
}