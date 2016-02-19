/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hostdiscovery.app;

import java.util.Collections;
import java.util.List;
import java.io.IOException;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.DeviceId;
import org.onosproject.cli.net.DeviceIdCompleter;

import org.onosproject.cli.Comparators;
import org.onosproject.net.Host;
import org.onosproject.net.host.HostService;
import org.onosproject.net.HostLocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static com.google.common.collect.Lists.newArrayList;
/**
 * Sample Apache Karaf CLI command
 */
@Command(scope = "onos", name = "host-dis",
         description = "Discovery Hosts which access to device")
public class Hostdiscommand extends AbstractShellCommand {

    @Argument(index = 0, name = "DeviceId", description = "Address of device",
            required = true, multiValued = false)
    String deviceId = null;

    private static final String FMT =
            "Devide ID = %s, host id = %s, host mac = %s, " +
                    "host location = %s, host IP = %s%s";

    @Override
    protected void execute() {

        print("This command shows list of hosts");

        DeviceId device = DeviceId.deviceId(deviceId);

        HostService service = get(HostService.class);
        Iterable<Host> hosts = deviceId != null ?
                service.getConnectedHosts(device) : getSortedHosts(service);

        if (outputJson()) {
            print("%s", json(getSortedHosts(service)));
        } else {
            for (Host host : hosts) {
                printHost(host);
            }
        }

        print("End command");

    }

    // Produces JSON structure.
    private JsonNode json(Iterable<Host> hosts) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode result = mapper.createArrayNode();

        hosts.forEach(host -> result.add(jsonForEntity(host, Host.class)));
        return result;
    }

    /**
     * Returns the list of devices sorted using the device ID URIs.
     *
     * @param service device service
     * @return sorted device list
     */
    protected List<Host> getSortedHosts(HostService service) {
        List<Host> hosts = newArrayList(service.getHosts());

            Collections.sort(hosts, Comparators.ELEMENT_COMPARATOR);
        return hosts;
    }

    /**
     * Prints information about a host.
     *
     * @param host end-station host
     */
    protected void printHost(Host host) {
        if (host != null) {
            print(FMT, deviceId, host.id(),host.mac(),
                      host.location().deviceId(), host.ipAddresses(),
                      annotations(host.annotations()));
        } else {
            print("no host");
        }
    }
}

