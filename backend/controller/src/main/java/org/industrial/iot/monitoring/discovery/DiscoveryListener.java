package org.industrial.iot.monitoring.discovery;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryListener implements ServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryListener.class);

    @Override
    public void serviceAdded(ServiceEvent event) {
        // Add the device to the database
        LOGGER.info("Service added: " + event.getInfo());
        System.out.println("Service added: " + event.getInfo());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        System.out.println("Service removed: " + event.getInfo());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        System.out.println("Service resolved: " + event.getInfo());
    }

}
