# IPv6-host-mobility-management
IPv6 host Mobility Management for ONOS Controller 

IPv6 host mobility management

Introduction: This module supports a network-based mobility management by using the OpenFlow-enabled AP (OF_AP) and ONOS controller. It consists of mobility control module and mobility information server in ONOS controller. The mobility control module handles the signaling messages and decision logic. The mobility information server module takes care of entries managing the mapping information associated with host-ID, AP-ID, and prefix information.

Architecture and Operation: 
 IPv6-host-mobility-management/image/Architecture.png
 
MN sends RS message to receive RA message which includes Prefix address. Once receiving the RS message in OF_AP, it is encapsulated with the OpenFlow Packet_In message and sends to the ONOS controller. In the mobility control module, parsing host ID and AP-ID from the received RS message and transferring them to the mobility information server module. In the mobility information server module, there is entry which include host-id, AP-id, Prefix address for IPv6 host. If there is no entry for the host, it will make a new entry or if there is the entry, it will update or maintain entry. The entry information (Prefix address) is delivered to the mobility control module to create the RA message and encapsulate it with the OpenFlow Packet_Out message. This OpenFlow Packet_Out message will send to OF_AP and OF_AP will decapsulate it and delivery RA message to MN

Command:
-	cfg set org.onosproject.provider.host.impl.HostLocationProvider ipv6NeighborDiscovery true
-	cfg set org.onosproject.proxyarp.ProxyArp ipv6NeighborDiscovery true
-	App activate org.onosproject.mobilitycontrol
-	App activate org.onosproject.mobilityinformationserver
