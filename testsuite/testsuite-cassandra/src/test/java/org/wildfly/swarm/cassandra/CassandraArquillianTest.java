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
package org.wildfly.swarm.cassandra;

import java.util.concurrent.TimeoutException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;

import com.datastax.driver.core.Cluster;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.arquillian.CreateSwarm;
import org.wildfly.swarm.arquillian.DefaultDeployment;
import org.wildfly.swarm.spi.api.OutboundSocketBinding;
import org.wildfly.swarm.config.cassandradriver.Cassandra;
import org.wildfly.swarm.config.cassandradriver.cassandra.Host;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Scott Marlow
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class CassandraArquillianTest {

    @CreateSwarm
    public static Swarm newSwarm() throws Exception {
        return new Swarm(false)

                .outboundSocketBinding("standard-sockets",
                        new OutboundSocketBinding("casstesthost")
                                .remoteHost("localhost")
                                .remotePort(9042))
                .fraction(new CassandraFraction()
                .cassandra(new Cassandra("cassandratestprofile")
                        .host(new Host("casstesthost")
                            .outboundSocketBindingRef("casstesthost")
                        )
                        .jndiName("java:jboss/cassandradriver/test")
                        .id("cassandratestprofile")
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
        Object cassandra = context.lookup("java:jboss/cassandradriver/test");
        assertNotNull(cassandra);
    }
    
    @Inject
    @Named("cassandratestprofile")
    private Cluster connection;
   
    @Test
    public void injectDatabaseConnection() throws Exception {
       assertNotNull(connection);
    }

}