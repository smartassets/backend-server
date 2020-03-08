package org.industrial.iot.monitoring.application;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationHostsRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHostsRetriever.class);

    public Set<String> getHosts() throws SocketException {
        List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        return networkInterfaces.stream()
                                .filter(this::isValid)
                                .map(NetworkInterface::getInterfaceAddresses)
                                .flatMap(List::stream)
                                .filter(this::hasBroadcast)
                                .map(interfaceAddress -> interfaceAddress.getAddress()
                                                                         .getHostAddress())
                                .collect(Collectors.toSet());
    }

    private boolean isValid(NetworkInterface networkInterface) {
        try {
            return !networkInterface.isLoopback() && networkInterface.isUp() && networkInterface.getHardwareAddress() != null;
        } catch (SocketException e) {
            // ignore in case of error
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean hasBroadcast(InterfaceAddress interfaceAddress) {
        return interfaceAddress.getBroadcast() != null;
    }
}
