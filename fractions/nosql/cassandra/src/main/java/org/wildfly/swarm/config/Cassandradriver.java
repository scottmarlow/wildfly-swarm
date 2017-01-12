package org.wildfly.swarm.config;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import org.wildfly.swarm.config.runtime.Implicit;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.cassandradriver.CassandraConsumer;
import org.wildfly.swarm.config.cassandradriver.CassandraSupplier;
import org.wildfly.swarm.config.cassandradriver.Cassandra;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * Cassandra driver subsystem
 */
@Address("/subsystem=cassandradriver")
@ResourceType("subsystem")
@Implicit
public class Cassandradriver<T extends Cassandradriver<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private CassandradriverResources subresources = new CassandradriverResources();

	public Cassandradriver(String key) {
		super();
		this.key = key;
		this.pcs = new PropertyChangeSupport(this);
	}

	public Cassandradriver() {
		super();
		this.key = "cassandradriver";
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

	public CassandradriverResources subresources() {
		return this.subresources;
	}

	/**
	 * Add all Cassandra objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Cassandra objects.
	 */
	@SuppressWarnings("unchecked")
	public T cassandras(java.util.List<Cassandra> value) {
		this.subresources.cassandras = value;
		return (T) this;
	}

	/**
	 * Add the Cassandra object to the list of subresources
	 * 
	 * @param value
	 *            The Cassandra to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T cassandra(Cassandra value) {
		this.subresources.cassandras.add(value);
		return (T) this;
	}

	/**
	 * Create and configure a Cassandra object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Cassandra resource
	 * @param config
	 *            The CassandraConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T cassandra(java.lang.String childKey, CassandraConsumer consumer) {
		Cassandra<? extends Cassandra> child = new Cassandra<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		cassandra(child);
		return (T) this;
	}

	/**
	 * Create and configure a Cassandra object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Cassandra resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T cassandra(java.lang.String childKey) {
		cassandra(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Cassandra object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T cassandra(CassandraSupplier supplier) {
		cassandra(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Cassandradriver
	 */
	public static class CassandradriverResources {
		/**
		 * Cassandra server definition
		 */
		@SubresourceInfo("cassandra")
		private List<Cassandra> cassandras = new java.util.ArrayList<>();

		/**
		 * Get the list of Cassandra resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Cassandra> cassandras() {
			return this.cassandras;
		}

		public Cassandra cassandra(java.lang.String key) {
			return this.cassandras.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}
}
