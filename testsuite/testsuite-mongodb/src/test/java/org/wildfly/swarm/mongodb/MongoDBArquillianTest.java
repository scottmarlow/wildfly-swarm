/**
 * Copyright 2016 Red Hat, Inc, and individual contributors.
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
package org.wildfly.swarm.mongodb;

import java.util.concurrent.TimeoutException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;

import com.mongodb.client.MongoDatabase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.arquillian.CreateSwarm;
import org.wildfly.swarm.arquillian.DefaultDeployment;
import org.wildfly.swarm.spi.api.OutboundSocketBinding;
import org.wildfly.swarm.config.mongodb.Mongo;
import org.wildfly.swarm.config.mongodb.mongo.Host;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Scott Marlow
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class MongoDBArquillianTest {

    @CreateSwarm
    public static Swarm newSwarm() throws Exception {
        return new Swarm(false)

                .outboundSocketBinding("standard-sockets",
                        new OutboundSocketBinding("mongotesthost")
                                .remoteHost("localhost")
                                .remotePort(27017))
/*
                .fraction(new ManagementFraction()
                        .securityRealm("mongoRealm", (realm) -> {
                            realm.inMemoryAuthentication( (authn)->{
                                authn.add( "devuser", "changethis", false );
                            });
                        }))
*/
                .fraction(new MongoDBFraction()
                .mongo(new Mongo("mongodbtestprofile")
                        .host(new Host("mongotesthost")
                            .outboundSocketBindingRef("mongotesthost")
                        )
                        .database("mongotestdb")
                        .jndiName("java:jboss/mongodb/test")
                        .id("mongodbtestprofile")
                        // .securityDomain("mongoRealm")

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
        Object mongoDB = context.lookup("java:jboss/mongodb/test");
        assertNotNull(mongoDB);
        assertTrue(mongoDB instanceof MongoDatabase);
    }

    @Inject
    @Named("mongodbtestprofile")
    MongoDatabase database;

    @Test
    public void injectDatabaseConnection() throws Exception {
       assertNotNull(database);
    }

}
