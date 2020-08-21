package org.industrial.iot.monitoring.application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Set;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializationBean implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializationBean.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ApplicationHostsRetriever hostsRetriever = new ApplicationHostsRetriever();
        Set<String> applicationHosts = hostsRetriever.getHosts();
        LOGGER.info(MessageFormat.format("Application hosts: \"{0}\"", applicationHosts));
        applicationHosts.stream().forEach(this::registerDnsService);
    }
    
    private void registerDnsService(String host) {
        try {
            LOGGER.info(MessageFormat.format("Registering service for host \"{0}\"", host));
            JmDNS jmdns = JmDNS.create(InetAddress.getByName(host));
            jmdns.registerService(ServiceInfo.create("_espserver._http.local", "backend", 8082, "path=/"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        // try {
        // Service service = Service.fromName("_esp._tcp");
        // Query query = Query.createFor(service, Domain.fromName("local."));
        // Set<Instance> instances = query.runOnceOn(InetAddress.getByName("192.168.1.104"));
        //
        // instances.stream()
        // .forEach(System.out::println);
        //
        // } catch (Exception e) {
        // // ignore
        // }
        // Arrays.asList(byName)
        // .stream()
        // .forEach(System.out::println);

        // System.out.println("Host addr: " + InetAddress.getLocalHost().getHostAddress()); // often returns "127.0.0.1"

        // The first one is your IP
        JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
        // jmdns.requestServiceInfo("_esp._tcp.local", "dadaD");
        jmdns.registerService(ServiceInfo.create("_espserver._http.local", "backend", 8082, "path=/"));
        // Wait a bit
        Thread.sleep(60000);
    }

}
