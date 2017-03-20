/**
 * Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.neo4j;

import java.util.concurrent.TimeoutException;
import javax.naming.InitialContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.arquillian.CreateSwarm;
import org.wildfly.swarm.arquillian.DefaultDeployment;
import org.wildfly.swarm.spi.api.OutboundSocketBinding;
import org.wildfly.swarm.config.neo4jdriver.Neo4j;
import org.wildfly.swarm.config.neo4jdriver.neo4j.Host;

import org.neo4j.driver.v1.Driver;

import static org.junit.Assert.assertNotNull;

/**
 * @author Scott Marlow
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class Neo4jArquillianTest {

    @CreateSwarm
    public static Swarm newSwarm() throws Exception {
        return new Swarm(false)

                .outboundSocketBinding("standard-sockets",
                        new OutboundSocketBinding("neo4jtesthost")
                                .remoteHost("localhost")
                                .remotePort(9042))
                .fraction(new Neo4jFraction()
                .neo4j(new Neo4j("neo4jtestprofile")
                        .host(new Host("neo4jtesthost")
                            .outboundSocketBindingRef("neo4jtesthost")
                        )
                        .jndiName("java:jboss/neo4jdriver/test")
                        .id("neo4jtestprofile")
                )
        );
    }

    @Test
    public void testNothing() throws InterruptedException, TimeoutException {
    }

    @ArquillianResource
    InitialContext context;

    @Test
    public void resourceLookup() throws Exception {
        Object connection = context.lookup("java:jboss/neo4jdriver/test");
        assertNotNull(connection);
    }

    @Inject
    @Named("neo4jtestprofile")
    Driver database;
    // Object database;

    @Test
    public void injectDatabaseConnection() throws Exception {
        assertNotNull(database);
    }

}