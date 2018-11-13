package com.gmail.eksuzyan.pavel.transfer.money.server;

import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.RestProperties;
import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.file.FileRestProperties;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Entry point for simulating access and manipulation with RESTful API for user account processing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/19/2018.
 */
public final class Backend {

    public static void main(String[] args) {
        try {
            RestProperties props = new FileRestProperties();
            try (BackendRunner ignored = new BackendRunner(props)) {
                waitForUserStop();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private static void waitForUserStop() throws Exception {
        System.out.println("Press any key to stop server. ");
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            console.readLine();
        }
    }

}
