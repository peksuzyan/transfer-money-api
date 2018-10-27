package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * User accounts wrapper in order to be represented in public API.
 * <p>
 * Not thread-safe.
 *
 * @author Pavel Eksuzian.
 * Created: 26.10.2018.
 */
@XmlRootElement
public class AccountsWrapper {

    /**
     * User account wrappers.
     */
    @XmlElement(name = "accounts")
    private Collection<AccountWrapper> accountWrappers;

    /**
     * Default constructor.
     */
    public AccountsWrapper() {
    }

    /**
     * Constructor with user account wrappers.
     *
     * @param accountWrappers user account wrappers
     */
    public AccountsWrapper(Collection<AccountWrapper> accountWrappers) {
        this.accountWrappers = accountWrappers;
    }

    /**
     * Gets user account wrappers.
     *
     * @return user account wrappers
     */
    public Collection<AccountWrapper> getAccountWrappers() {
        return accountWrappers;
    }

    /**
     * Sets user account wrappers.
     *
     * @param accountWrappers user account wrappers
     */
    public void setAccountWrappers(Collection<AccountWrapper> accountWrappers) {
        this.accountWrappers = accountWrappers;
    }
}
