/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.sample.isanalyticsclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.databridge.agent.AgentHolder;
import org.wso2.carbon.databridge.agent.DataPublisher;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.databridge.commons.utils.DataBridgeCommonsUtils;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * WSO2 Identity Server Analytics - Sample Event Client
 */
public class Client {

    private static final Log log = LogFactory.getLog(Client.class);
    private static final String STREAM_NAME = "org.wso2.is.analytics.stream.OverallAuthentication";
    private static final String VERSION = "1.0.0";
    private static final String agentConfigFileName = "sync.data.agent.config.yaml";

    public static void main(String[] args) {

        DataPublisherUtil.setKeyStoreParams();
        DataPublisherUtil.setTrustStoreParams();

        log.info("These are the provided configurations: " + Arrays.deepToString(args));

        String protocol = args[0];
        String host = args[1];
        String port = args[2];
        int sslPort = Integer.parseInt(port) + 100;
        String username = args[3];
        String password = args[4];
        String numberOfEventsStr = args[5];
        int numberOfEvents = Integer.parseInt(numberOfEventsStr);
        int metaTenantIdMinBound = 1000;
        int metaTenantIdMaxBound = 9999;


        try {
            log.info("Starting IS Analytics Event Client");

            AgentHolder.setConfigPath(DataPublisherUtil.getDataAgentConfigPath(agentConfigFileName));
            DataPublisher dataPublisher = new DataPublisher(protocol, "tcp://" + host + ":" + port,
                    "ssl://" + host + ":" + sslPort, username, password);
            Event event = new Event();
            event.setStreamId(DataBridgeCommonsUtils.generateStreamId(STREAM_NAME, VERSION));
            event.setCorrelationData(null);

            for (int i = 0; i < numberOfEvents; i++) {
                int metaTenantId = ThreadLocalRandom.current().nextInt(metaTenantIdMinBound, metaTenantIdMaxBound);
                Object[] data = getEventDataObject();
                event.setMetaData(new Object[]{metaTenantId});
                event.setPayloadData(data);
                dataPublisher.publish(event);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error(e);
            }
            dataPublisher.shutdown();
            log.info("Events published successfully");

        } catch (Throwable e) {
            log.error(e);
        }
    }

    private static Object[] getEventDataObject() {
        String[] usernames = {
                "admin",
                "thisaru",
                "LordVoldemort",
                "ProfMoriyarty",
                "Menaka",
                "Ruwangika",
                "Irunika",
                "Ninada",
                "Tharinda",
                "Dilanka",
                "Namodaya",
                "StrongArm",
                "Karl",
                "Neil",
                "Aravinda"
        };

        String[] localUsernames = {
                "admin",
                "sam",
                "TomRiddle",
                "TheOne",
                "Mana",
                "Ru",
                "Iru",
                "Nin",
                "Thari",
                "Dila",
                "Namo",
                "Gun",
                "Scientist",
                "Atheist",
                "Silva"
        };

        String[] userRoles = {
                "admin",
                "user",
                "admin, user",
                "restricted",
                "user, restricted"
        };

        String[] ipAddresses = {
                "152.101.145.121",
                "8.89.153.229",
                "167.72.51.148",
                "35.155.233.154",
                "165.211.184.166",
                "150.88.129.200",
                "164.28.204.222",
                "75.177.7.40",
                "66.124.5.23",
                "254.107.47.83",
                "213.138.26.248",
                "185.168.135.92",
                "45.1.8.19",
                "30.47.231.32",
                "65.208.246.181"
        };

        String[] eventTypes = {"step", "overall", "test-type"};
        String[] userStoreDomains = {"user-store-1", "default-store", "testing-user-store"};
        String[] tenantDomains = {"tenant-domain-1", "custom-tenant", "sample-tenant"};
        String[] inboundAuthTypes = {"inbound-auth-1", "sample-inbound-auth", "custom-inbound-auth"};
        String[] serviceProviders = {"travelocity.com", "harrypotter.com", "sherlock.org"};
        String[] authenticationSteps = {"general", "2-step-auth", "SAML"};
        String[] identityProviders = {"google.com", "facebook.com", "amazon.com"};
        String[] stepAuthenticators = {"email", "totp", "motp"};
        String[] identityProviderTypes = {"LOCAL", "FEDERATED"};

        String contextId, eventId, eventType, username, localUsername, userStoreDomain, tenantDomain, remoteIp,
                inboundAuth, serviceProvider, rolesCommaSeparated, authenticationStep, identityProvider,
                stepAuthenticator, identityProviderType;
        Boolean authenticationSuccess, rememberMeEnabled, forceAuthEnabled, passiveAuthEnabled, authStepSuccess,
                isFirstLogin;
        int index = ThreadLocalRandom.current().nextInt(0, 15);
        contextId = UUID.randomUUID().toString();
        eventId = UUID.randomUUID().toString();
        eventType = eventTypes[index % 3];
        authenticationSuccess = ThreadLocalRandom.current().nextBoolean();
        username = usernames[index];
        localUsername = localUsernames[index];
        userStoreDomain = userStoreDomains[index % 3];
        tenantDomain = tenantDomains[index % 3];
        inboundAuth = inboundAuthTypes[index % 3];
        remoteIp = ipAddresses[index];
        serviceProvider = serviceProviders[index % 3];
        rememberMeEnabled = ThreadLocalRandom.current().nextBoolean();
        forceAuthEnabled = ThreadLocalRandom.current().nextBoolean();
        passiveAuthEnabled = ThreadLocalRandom.current().nextBoolean();
        rolesCommaSeparated = userRoles[index % 5];
        authenticationStep = authenticationSteps[index % 3];
        identityProvider = identityProviders[index % 3];
        authStepSuccess = authenticationSuccess;
        stepAuthenticator = stepAuthenticators[index % 3];
        identityProviderType = identityProviderTypes[index % 2];
        isFirstLogin = ThreadLocalRandom.current().nextBoolean();
        Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();

        return (new Object[]{
                contextId,
                eventId,
                eventType,
                authenticationSuccess,
                username,
                localUsername,
                userStoreDomain,
                tenantDomain,
                remoteIp,
                "N/A",
                inboundAuth,
                serviceProvider,
                rememberMeEnabled,
                forceAuthEnabled,
                passiveAuthEnabled,
                rolesCommaSeparated,
                authenticationStep,
                identityProvider,
                authStepSuccess,
                stepAuthenticator,
                isFirstLogin,
                identityProviderType,
                timestamp
        });
    }
}
