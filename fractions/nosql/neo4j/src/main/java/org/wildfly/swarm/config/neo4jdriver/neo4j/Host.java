package org.wildfly.swarm.config.neo4jdriver.neo4j;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * Host
 */
@Address("/subsystem=neo4jdriver/neo4j=*/host=*")
@ResourceType("host")
public class Host<T extends Host<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private String outboundSocketBindingRef;

	public Host(java.lang.String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return this.key;
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

	/**
	 * Neo4J target hostname/port number
	 */
	@ModelNodeBinding(detypedName = "outbound-socket-binding-ref")
	public String outboundSocketBindingRef() {
		return this.outboundSocketBindingRef;
	}

	/**
	 * Neo4J target hostname/port number
	 */
	@SuppressWarnings("unchecked")
	public T outboundSocketBindingRef(java.lang.String value) {
		Object oldValue = this.outboundSocketBindingRef;
		this.outboundSocketBindingRef = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("outboundSocketBindingRef", oldValue,
					value);
		return (T) this;
	}
}