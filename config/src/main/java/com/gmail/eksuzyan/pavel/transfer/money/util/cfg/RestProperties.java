package com.gmail.eksuzyan.pavel.transfer.money.util.cfg;

/**
 * Facade is used to have an access to rest-specific properties.
 *
 * @author Pavel Eksuzian.
 *         Created: 06.11.2018.
 */
public interface RestProperties {

    /**
     * Gets server scheme property.
     *
     * @return server scheme
     */
    String getServerScheme();

    /**
     * Gets server host property.
     *
     * @return server host
     */
    String getServerHost();

    /**
     * Gets server port property.
     *
     * @return server port
     */
    Integer getServerPort();

}
