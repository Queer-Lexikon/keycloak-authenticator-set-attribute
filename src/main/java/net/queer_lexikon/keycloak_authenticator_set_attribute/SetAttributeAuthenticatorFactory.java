package net.queer_lexikon.keycloak_authenticator_set_attribute;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class SetAttributeAuthenticatorFactory implements AuthenticatorFactory {
    public static final String PROVIDER_ID = "set-attribute-authenticator";
    private static final SetAttributeAuthenticator SINGLETON = new SetAttributeAuthenticator();

    @Override
    public String getDisplayType() {
        return "Set User Attribute";
    }

    @Override
    public String getReferenceCategory() {
        return "Set User Attribute";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.DISABLED};
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Allows the user to provide a value for a user attribute.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> properties = new ArrayList<ProviderConfigProperty>();

        properties.add(
                new ProviderConfigProperty(
                        "userAttribute",
                        "User Attribute",
                        "The user attribute to set.",
                        ProviderConfigProperty.STRING_TYPE,
                        ""));

        properties.add(
                new ProviderConfigProperty(
                        "skipIfSet",
                        "Skip If Set",
                        "Skip this form if the attribute is already set",
                        ProviderConfigProperty.BOOLEAN_TYPE,
                        true));

        properties.add(
                new ProviderConfigProperty(
                        "inputLabel",
                        "Form Input Label",
                        "The label for the input field of the form displayed to the user,",
                        ProviderConfigProperty.TEXT_TYPE,
                        ""));

        properties.add(
                new ProviderConfigProperty(
                        "usernameDefault",
                        "Username as Default",
                        "Provide the username as default input.",
                        ProviderConfigProperty.BOOLEAN_TYPE,
                        false));

        properties.add(
                new ProviderConfigProperty(
                        "validationRegex",
                        "Validation Regex",
                        "Regex for validating the user input",
                        ProviderConfigProperty.STRING_TYPE, ""));

        properties.add(
                new ProviderConfigProperty(
                        "validationError",
                        "Validation Error Message",
                        "Error message, if validation faile",
                        ProviderConfigProperty.TEXT_TYPE, ""));

        return properties;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
