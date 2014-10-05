package org.wormsim.frontend.stormpath;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;

public class ClientFactory {

    private static final String APP_NAME = "Wormsim";
    private static final String APIKEY_FILEPATH = System.getProperty("user.home")+"/.stormpath/apiKey.properties";
    private static final Client CLIENT;
    private static final Application APPLICATION;

    static {
        CLIENT = Clients.builder().setApiKey(ApiKeys.builder().setFileLocation(APIKEY_FILEPATH).build()).build();

        ApplicationList applications = CLIENT.getApplications(
                Applications.where(Applications.name().eqIgnoreCase(APP_NAME))
        );

        APPLICATION = applications.iterator().next();
    }

    public static Account createAccount(String email, String password, String firstName, String lastName) {
        Account account = CLIENT.instantiate(Account.class);
        account.setGivenName(firstName);
        account.setSurname(lastName);
        account.setEmail(email);
        account.setPassword(password);
        return APPLICATION.createAccount(account);
    }
}
