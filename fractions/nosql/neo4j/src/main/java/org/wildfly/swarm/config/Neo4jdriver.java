package org.wildfly.swarm.config;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import org.wildfly.swarm.config.runtime.Implicit;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.neo4jdriver.Neo4jConsumer;
import org.wildfly.swarm.config.neo4jdriver.Neo4jSupplier;
import org.wildfly.swarm.config.neo4jdriver.Neo4j;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * Neo4J driver subsystem
 */
@Address("/subsystem=neo4jdriver")
@ResourceType("subsystem")
@Implicit
public class Neo4jdriver<T extends Neo4jdriver<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private Neo4jdriverResources subresources = new Neo4jdriverResources();

       public Neo4jdriver(String key) {
                super();
                this.key = key;
                this.pcs = new PropertyChangeSupport(this);
        }

	public Neo4jdriver() {
		super();
		this.key = "neo4jdriver";
		this.pcs = new PropertyChangeSupport(this);
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

	public Neo4jdriverResources subresources() {
		return this.subresources;
	}

	/**
	 * Add all Neo4j objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Neo4j objects.
	 */
	@SuppressWarnings("unchecked")
	public T neo4js(java.util.List<Neo4j> value) {
		this.subresources.neo4js = value;
		return (T) this;
	}

	/**
	 * Add the Neo4j object to the list of subresources
	 * 
	 * @param value
	 *            The Neo4j to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T neo4j(Neo4j value) {
		this.subresources.neo4js.add(value);
		return (T) this;
	}

	/**
	 * Create and configure a Neo4j object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Neo4j resource
	 * @param config
	 *            The Neo4jConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T neo4j(java.lang.String childKey, Neo4jConsumer consumer) {
		Neo4j<? extends Neo4j> child = new Neo4j<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		neo4j(child);
		return (T) this;
	}

	/**
	 * Create and configure a Neo4j object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Neo4j resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T neo4j(java.lang.String childKey) {
		neo4j(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Neo4j object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T neo4j(Neo4jSupplier supplier) {
		neo4j(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Neo4jdriver
	 */
	public static class Neo4jdriverResources {
		/**
		 * Neo4J server definition
		 */
		@SubresourceInfo("neo4j")
		private List<Neo4j> neo4js = new java.util.ArrayList<>();

		/**
		 * Get the list of Neo4j resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Neo4j> neo4js() {
			return this.neo4js;
		}

		public Neo4j neo4j(java.lang.String key) {
			return this.neo4js.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}
}
