package org.wildfly.swarm.config.neo4jdriver;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.neo4jdriver.neo4j.HostConsumer;
import org.wildfly.swarm.config.neo4jdriver.neo4j.HostSupplier;
import org.wildfly.swarm.config.neo4jdriver.neo4j.Host;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * Neo4J server definition
 */
@Address("/subsystem=neo4jdriver/neo4j=*")
@ResourceType("neo4j")
public class Neo4j<T extends Neo4j<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private Neo4jResources subresources = new Neo4jResources();
	private String id;
	private String jndiName;
	private String module;
	private String transaction;

	public Neo4j(java.lang.String key) {
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

	public Neo4jResources subresources() {
		return this.subresources;
	}

	/**
	 * Add all Host objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Host objects.
	 */
	@SuppressWarnings("unchecked")
	public T hosts(java.util.List<Host> value) {
		this.subresources.hosts = value;
		return (T) this;
	}

	/**
	 * Add the Host object to the list of subresources
	 * 
	 * @param value
	 *            The Host to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T host(Host value) {
		this.subresources.hosts.add(value);
		return (T) this;
	}

	/**
	 * Create and configure a Host object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Host resource
	 * @param config
	 *            The HostConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T host(java.lang.String childKey, HostConsumer consumer) {
		Host<? extends Host> child = new Host<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		host(child);
		return (T) this;
	}

	/**
	 * Create and configure a Host object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Host resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T host(java.lang.String childKey) {
		host(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Host object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T host(HostSupplier supplier) {
		host(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Neo4j
	 */
	public static class Neo4jResources {
		/**
		 * Host
		 */
		@SubresourceInfo("host")
		private List<Host> hosts = new java.util.ArrayList<>();

		/**
		 * Get the list of Host resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Host> hosts() {
			return this.hosts;
		}

		public Host host(java.lang.String key) {
			return this.hosts.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}

	/**
	 * Unique profile identification
	 */
	@ModelNodeBinding(detypedName = "id")
	public String id() {
		return this.id;
	}

	/**
	 * Unique profile identification
	 */
	@SuppressWarnings("unchecked")
	public T id(java.lang.String value) {
		Object oldValue = this.id;
		this.id = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("id", oldValue, value);
		return (T) this;
	}

	/**
	 * JNDI address
	 */
	@ModelNodeBinding(detypedName = "jndi-name")
	public String jndiName() {
		return this.jndiName;
	}

	/**
	 * JNDI address
	 */
	@SuppressWarnings("unchecked")
	public T jndiName(java.lang.String value) {
		Object oldValue = this.jndiName;
		this.jndiName = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("jndiName", oldValue, value);
		return (T) this;
	}

	/**
	 * Module name
	 */
	@ModelNodeBinding(detypedName = "module")
	public String module() {
		return this.module;
	}

	/**
	 * Module name
	 */
	@SuppressWarnings("unchecked")
	public T module(java.lang.String value) {
		Object oldValue = this.module;
		this.module = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("module", oldValue, value);
		return (T) this;
	}

	/**
	 * Transaction enlistment (none or 1pc)
	 */
	@ModelNodeBinding(detypedName = "transaction")
	public String transaction() {
		return this.transaction;
	}

	/**
	 * Transaction enlistment (none or 1pc)
	 */
	@SuppressWarnings("unchecked")
	public T transaction(java.lang.String value) {
		Object oldValue = this.transaction;
		this.transaction = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("transaction", oldValue, value);
		return (T) this;
	}
}