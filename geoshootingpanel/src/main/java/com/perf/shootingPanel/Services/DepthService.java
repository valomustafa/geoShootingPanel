/*
 *   GEODynamics
 *   Author - James Suderman
 *   Date - 06/19/2020
 */

package com.perf.shootingPanel.Services;

import com.fazecast.jSerialComm.SerialPort;
import com.perf.shootingPanel.controllers.AppController;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.io.InputStream;

public class DepthService implements Runnable {
    private static final Logger depthLogger = Logger.getLogger(DepthService.class);
    private final AppController controller;
    private volatile boolean running = true;
    private StringBuilder sb = new StringBuilder();
    private double lastDepth = 0.0;

    public DepthService(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPort port = null;
        InputStream in = null;

        for(SerialPort p : ports) {
            if(p.getDescriptivePortName().equals("USB-to-Serial Port (ftdi_sio)")) {
                port = p;
            }
        }

        if(port != null) {
            port.openPort();
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            in = port.getInputStream();
        }

        while(running) {
            try {
                    while(port.bytesAvailable() == 0) {
                        Thread.sleep(20);
                    }

                    int data = in.read();

                    if(data != 13 && data != 10) {
                        if(data != 32) {
                            sb.append((char) data);
                        }
                    } else {
                        double currentDepth = Double.parseDouble(sb.toString());
                        if(currentDepth != lastDepth) {
                            Platform.runLater(() -> controller.handleDepth(currentDepth));
                            lastDepth = currentDepth;
                        }
                        sb = new StringBuilder();
                    }

            } catch (Exception e) {
                sb = new StringBuilder();
            }
        }

        sb = new StringBuilder();

        Platform.runLater(() -> controller.deactivateDepth());

        lastDepth = 0.0;
        this.running = true;

        if(port != null) {
            port.closePort();
        }
    }

    public void stop() {
        this.running = false;
    }
}