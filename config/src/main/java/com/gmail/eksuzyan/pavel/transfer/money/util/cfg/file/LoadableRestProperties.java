package com.gmail.eksuzyan.pavel.transfer.money.util.cfg.file;

import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.RestProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * Loads file properties and provides an access to rest-specific properties.
 *
 * @author Pavel Eksuzian.
 * Created: 06.11.2018.
 */
public final class LoadableRestProperties extends Properties implements RestProperties {

    private static final Path PROPERTIES_FILE_PATH = Paths.get("server.properties").toAbsolutePath();

    private static final String SERVER_SCHEME_PROP_NAME = "server.scheme";
    private static final String SERVER_HOST_PROP_NAME = "server.host";
    private static final String SERVER_PORT_PROP_NAME = "server.port";

    /**
     * Default constructor.
     *
     * @throws IllegalStateException if any I/O errors occurred
     */
    public LoadableRestProperties() {
        boolean loaded = loadFileProps();

        putIfAbsent(SERVER_SCHEME_PROP_NAME, "http");
        putIfAbsent(SERVER_HOST_PROP_NAME, "localhost");
        putIfAbsent(SERVER_PORT_PROP_NAME, String.valueOf(9999));

        if (!loaded)
            createNewFileProps();
    }

    private void createNewFileProps() {
        try (OutputStream os = newOutputStream(PROPERTIES_FILE_PATH)) {
            store(os, null);
            System.out.println("Default properties are written in " + PROPERTIES_FILE_PATH);
        } catch (IOException e) {
            throw new IllegalStateException("Properties cannot be stored in " + PROPERTIES_FILE_PATH, e);
        }
    }

    private boolean loadFileProps() {
        try (InputStream is = newInputStream(PROPERTIES_FILE_PATH)) {
            load(is);
            return true;
        } catch (NoSuchFileException e) {
            System.out.println("Properties file does not exist in " + PROPERTIES_FILE_PATH);
            return false;
        } catch (IOException e) {
            throw new IllegalStateException("Properties cannot be loaded from " + PROPERTIES_FILE_PATH, e);
        }
    }

    @Override
    public String getServerScheme() {
        return getProperty(SERVER_SCHEME_PROP_NAME);
    }

    @Override
    public String getServerHost() {
        return getProperty(SERVER_HOST_PROP_NAME);
    }

    @Override
    public Integer getServerPort() {
        return Integer.valueOf(getProperty(SERVER_PORT_PROP_NAME));
    }
}
