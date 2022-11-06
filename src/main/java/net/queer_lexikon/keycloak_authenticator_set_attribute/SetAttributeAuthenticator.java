package net.queer_lexikon.keycloak_authenticator_set_attribute;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.jboss.logging.Logger;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class SetAttributeAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(SetAttributeAuthenticator.class);

    private Response createAskAttributeForm(AuthenticationFlowContext context, LoginFormsProvider formsProvider){
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();

        String defaultValue = Objects.equals(config.get("usernameDefault"), "true") ? context.getUser().getUsername() : "";
        return createAskAttributeForm(context, formsProvider, defaultValue);
    }

    private Response createAskAttributeForm(AuthenticationFlowContext context, LoginFormsProvider formsProvider, String defaultValue) {
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();

        return formsProvider
                .setAttribute("config", config)
                .setAttribute("defaultValue", defaultValue)
                .createForm("set-attribute.ftl");
    }
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if(context.getAuthenticatorConfig() == null) {
            logger.error("Authenticator " + SetAttributeAuthenticatorFactory.PROVIDER_ID + " is not configured");
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        Map<String, String> config = context.getAuthenticatorConfig().getConfig();
        UserModel user = context.getUser();
        String userAttribute = config.get("userAttribute");

        if(userAttribute == null || userAttribute.equals("")) {
            logger.error("userAttribute is not configured for " + SetAttributeAuthenticatorFactory.PROVIDER_ID);
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }

        if(user.getFirstAttribute(userAttribute) != null && Objects.equals(config.get("skipIfSet"), "true")) {
            context.success();
            return;
        }

        context.challenge(
                createAskAttributeForm(context, context.form()));
    }



    @Override
    public void action(AuthenticationFlowContext context) {
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();
        UserModel user = context.getUser();
        String userAttribute = config.get("userAttribute");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        String inputValue = formData.getFirst(userAttribute);
        String regex = config.get("validationRegex");

        if(regex != null && !regex.equals("")) {
            if(!inputValue.matches(regex)) {
                context.failureChallenge(
                        AuthenticationFlowError.INVALID_CREDENTIALS,
                        createAskAttributeForm(
                                context,
                                context.form()
                                    .addError(new FormMessage(
                                            userAttribute,
                                            config.get("validationError"))),
                                inputValue));
                return;
            }
        }

        user.setAttribute(userAttribute, Collections.singletonList(inputValue));
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}