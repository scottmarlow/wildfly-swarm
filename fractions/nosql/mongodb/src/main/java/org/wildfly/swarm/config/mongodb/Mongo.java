package org.wildfly.swarm.config.mongodb;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.mongodb.mongo.HostConsumer;
import org.wildfly.swarm.config.mongodb.mongo.HostSupplier;
import org.wildfly.swarm.config.mongodb.mongo.Host;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.mongodb.mongo.PropertiesConsumer;
import org.wildfly.swarm.config.mongodb.mongo.PropertiesSupplier;
import org.wildfly.swarm.config.mongodb.mongo.Properties;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * MongoDB server definition
 */
@Address("/subsystem=mongodb/mongo=*")
@ResourceType("mongo")
public class Mongo<T extends Mongo<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private MongoResources subresources = new MongoResources();
	private String database;
	private String id;
	private String jndiName;
	private String module;
	private String securityDomain;

	public Mongo(java.lang.String key) {
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

	public MongoResources subresources() {
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
	 * Add all Properties objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Properties objects.
	 */
	@SuppressWarnings("unchecked")
	public T properties(java.util.List<Properties> value) {
		this.subresources.properties = value;
		return (T) this;
	}

	/**
	 * Add the Properties object to the list of subresources
	 * 
	 * @param value
	 *            The Properties to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T properties(Properties value) {
		this.subresources.properties.add(value);
		return (T) this;
	}

	/**
	 * Create and configure a Properties object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Properties resource
	 * @param config
	 *            The PropertiesConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T properties(java.lang.String childKey, PropertiesConsumer consumer) {
		Properties<? extends Properties> child = new Properties<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		properties(child);
		return (T) this;
	}

	/**
	 * Create and configure a Properties object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Properties resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T properties(java.lang.String childKey) {
		properties(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Properties object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T properties(PropertiesSupplier supplier) {
		properties(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Mongo
	 */
	public static class MongoResources {
		/**
		 * Host
		 */
		@SubresourceInfo("host")
		private List<Host> hosts = new java.util.ArrayList<>();
		/**
		 * Custom MongoDB properties
		 */
		@SubresourceInfo("properties")
		private List<Properties> properties = new java.util.ArrayList<>();

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
		/**
		 * Get the list of Properties resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Properties> properties() {
			return this.properties;
		}

		public Properties properties(java.lang.String key) {
			return this.properties.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}

	/**
	 * MongoDB database name
	 */
	@ModelNodeBinding(detypedName = "database")
	public String database() {
		return this.database;
	}

	/**
	 * MongoDB database name
	 */
	@SuppressWarnings("unchecked")
	public T database(java.lang.String value) {
		Object oldValue = this.database;
		this.database = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("database", oldValue, value);
		return (T) this;
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
	 * Security domain name
	 */
	@ModelNodeBinding(detypedName = "security-domain")
	public String securityDomain() {
		return this.securityDomain;
	}

	/**
	 * Security domain name
	 */
	@SuppressWarnings("unchecked")
	public T securityDomain(java.lang.String value) {
		Object oldValue = this.securityDomain;
		this.securityDomain = value;
		if (this.pcs != null)
			this.pcs.firePropertyChange("securityDomain", oldValue, value);
		return (T) this;
	}
}