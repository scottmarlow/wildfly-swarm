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
package org.wildfly.swarm.config;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import org.wildfly.swarm.config.runtime.Implicit;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * MONGODB
 *
 * @author Scott Marlow
 */
@Address("/subsystem=mongodb")
@ResourceType("subsystem")
@Implicit
public class MONGODB <T extends MONGODB<T>> extends HashMap implements
			org.wildfly.swarm.config.runtime.Keyed {

    private String key;
    private PropertyChangeSupport pcs;

    public MONGODB() {
   		super();
   		this.key = "mongodb";
   		this.pcs = new PropertyChangeSupport(this);
   	}

    /**
   	 * Adds a property change listener
   	 */
   	public void addPropertyChangeListener(PropertyChangeListener listener) {
   		if (null == this.pcs)
   			this.pcs = new PropertyChangeSupport(this);
   		this.pcs.addPropertyChangeListener(listener);
   	}

   	/**
   	 * Removes a property change listener
   	 */
   	public void removePropertyChangeListener(
   			java.beans.PropertyChangeListener listener) {
   		if (this.pcs != null)
   			this.pcs.removePropertyChangeListener(listener);
   	}

    @Override
    public String getKey() {
        return key;
    }
}
