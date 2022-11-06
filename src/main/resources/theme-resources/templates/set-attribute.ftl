<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
        ${msg("loginTitle",realm.name)}
    <#elseif section = "header">
        ${msg("loginTitleHtml",realm.name)}
    <#elseif section = "form">
        <form id="kc-totp-login-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="${config["userAttribute"]}" class="${properties.kcLabelClass!}">${config["inputLabel"]!("Please provide a value for " + config["userAttribute"])}</label>
                </div>

                <div class="${properties.kcInputWrapperClass!}">
                    <input id="${config["userAttribute"]}"
                           name="${config["userAttribute"]}"
                           type="text"
                           autocomplete="off"
                           value="${defaultValue}"
                           class="${properties.kcInputClass!}"
                           aria-invalid="<#if messagesPerField.existsError(config["userAttribute"])>true</#if>"/>
                    <#if messagesPerField.existsError(config["userAttribute"])>
                        <span id="input-error-${config["userAttribute"]}-label" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                            ${kcSanitize(messagesPerField.get(config["userAttribute"]))?no_esc}
                        </span>
                    </#if>
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>