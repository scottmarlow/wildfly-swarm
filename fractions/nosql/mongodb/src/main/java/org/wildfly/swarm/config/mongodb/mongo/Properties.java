package org.wildfly.swarm.config.mongodb.mongo;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;
import java.util.Map;

/**
 * Custom MongoDB properties
 */
@Address("/subsystem=mongodb/mongo=*/properties=*")
@ResourceType("properties")
public class Properties<T extends Properties<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private Map property;

	public Properties(java.lang.String key) {
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
	 * Custom MongoDB property
	 */
	@ModelNodeBinding(detypedName = "property")
	public Map property() {
		return this.property;
	}

	/**
	 * Custom MongoDB property
	 */
	@SuppressWarnings("unchecked")
	public T property(java.util.Map value) {
		Object oldValue = this.property;
		this.property = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("property", oldValue, value);
		return (T) this;
	}

	/**
	 * Custom MongoDB property
	 */
	@SuppressWarnings("unchecked")
	public T property(java.lang.String key, java.lang.Object value) {
		if (this.property == null) {
			this.property = new java.util.HashMap<>();
		}
		this.property.put(key, value);
		return (T) this;
	}
}