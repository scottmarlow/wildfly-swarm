/**
 * Copyright 2016 Red Hat, Inc, and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.mongodb;

import java.util.HashMap;
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
import org.wildfly.swarm.arquillian.CreateSwarm;
import org.wildfly.swarm.arquillian.DefaultDeployment;
import org.wildfly.swarm.spi.api.OutboundSocketBinding;
import org.wildfly.swarm.config.mongodb.Mongo;
import org.wildfly.swarm.config.mongodb.mongo.Host;
import org.wildfly.swarm.config.security.Flag;
import org.wildfly.swarm.config.security.SecurityDomain;
import org.wildfly.swarm.config.security.security_domain.ClassicAuthentication;
import org.wildfly.swarm.config.security.security_domain.authentication.LoginModule;
import org.wildfly.swarm.security.SecurityFraction;

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
                .fraction(SecurityFraction.defaultSecurityFraction()
                        .securityDomain(
                                new SecurityDomain("mongoRealm")
                                        .classicAuthentication(
                                                new ClassicAuthentication().loginModule(
                                                        new LoginModule("ConfiguredIdentity").code("ConfiguredIdentity")
                                                                .flag(Flag.REQUIRED)
                                                                .moduleOptions(new HashMap<Object, Object>() {
                                                                                   {
                                                                                       put("principal","devuser");
                                                                                       put("username","devuser");
                                                                                       put("password","changethis");
                                                                                   }
                                                                               }
                                                                )
                                                )
                                        )
                        )
                  )
                .fraction(new MongoDBFraction()
                        .mongo(new Mongo("mongodbtestprofile")
                                .host(new Host("mongotesthost")
                                        .outboundSocketBindingRef("mongotesthost")
                                )
                                .database("mongotestdb")
                                .jndiName("java:jboss/mongodb/test")
                                .id("mongodbtestprofile")
                                .securityDomain("mongoRealm")

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
