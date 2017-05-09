/*
 *
 *  * JBoss, Home of Professional Open Source.
 *  * Copyright 2017, Red Hat, Inc., and individual contributors
 *  * as indicated by the @author tags. See the copyright.txt file in the
 *  * distribution for a full listing of individual contributors.
 *  *
 *  * This is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as
 *  * published by the Free Software Foundation; either version 2.1 of
 *  * the License, or (at your option) any later version.
 *  *
 *  * This software is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public
 *  * License along with this software; if not, write to the Free
 *  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 *
 */

package org.wildfly.swarm.mongodb.runtime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.wildfly.swarm.mongodb.MongoDBFraction;
import org.wildfly.swarm.spi.api.Customizer;
import org.wildfly.swarm.spi.runtime.annotations.Pre;

/**
 * MongoDBCustomizer
 *
 * @author Scott Marlow
 */
@Pre
@ApplicationScoped
public class MongoDBCustomizer implements Customizer {

    @Inject
    @Any
    Instance<MongoDBFraction> allDrivers;

    @Override
    public void customize() throws Exception {
        customizeJDBCDrivers();
    }

    protected void customizeJDBCDrivers() {
            this.allDrivers.forEach(this::attemptInstallation);
        }

    protected void attemptInstallation(MongoDBFraction info) {
        MongoDBDriverInfo mongoDBDriverInfo = new MongoDBDriverInfo();
        if (mongoDBDriverInfo.detect(info)) {
            //    DatasourcesMessages.MESSAGES.autodetectedJdbcDriver(info.name());
        }
    }
}
