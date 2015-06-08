package org.wormsim.frontend.models;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountStatus;
import com.stormpath.sdk.directory.CustomData;
import org.wormsim.frontend.stormpath.AccountNotFoundException;
import org.wormsim.frontend.stormpath.ClientFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

    @NotNull
    private String email;
    @NotNull
    @Size(min=8, max=30)
    private String password;

    private String firstName;
    private String lastName;
    private String wormName;
    private String wormColor;    
    public String tutorialLoaded = "false";

    public static String TUTORIAL_LOADED = "tutorialLoaded";
    
    private Account account;

    public User() {
    }

    public User(Account account) {
        this.account = account;
        this.email = account.getEmail();
        this.firstName = account.getGivenName();
        this.lastName = account.getSurname();

        CustomData customData = account.getCustomData();
        this.wormName = (String) customData.get("wormName");
        this.wormColor = (String) customData.get("wormColor");
        
        String tutorialCompleted = (String) customData.get(TUTORIAL_LOADED);
        this.tutorialLoaded = (String) (tutorialCompleted != null ? tutorialCompleted : "false");
    }

    public void save() {

        if (account == null) {
            try {
                account = ClientFactory.getInstance().getAccount(this.email);
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }

        account.setStatus(AccountStatus.ENABLED);
        account.setEmail(this.email);
        account.setGivenName(this.firstName);
        account.setSurname(this.lastName);
        
        if (wormName != null && !wormName.isEmpty()) {
            account.getCustomData().put("wormName", wormName);
        }

        if(wormColor != null && !wormColor.isEmpty()) {
            account.getCustomData().put("wormColor", wormColor);
        }
        
        account.getCustomData().put(TUTORIAL_LOADED, tutorialLoaded);
        account.save();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getWormName() { return this.wormName; }

    public void setWormName(String wormName) { this.wormName = wormName; }

    public String getWormColor() { return this.wormColor; }

    public void setWormColor(String wormColor) { this.wormColor = wormColor; }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }
    
    
}
