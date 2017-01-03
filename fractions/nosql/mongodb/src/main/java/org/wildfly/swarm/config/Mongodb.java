package org.wildfly.swarm.config;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import org.wildfly.swarm.config.runtime.Implicit;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.mongodb.MongoConsumer;
import org.wildfly.swarm.config.mongodb.MongoSupplier;
import org.wildfly.swarm.config.mongodb.Mongo;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * MongoDB driver subsystem
 */
@Address("/subsystem=mongodb")
@ResourceType("subsystem")
@Implicit
public class Mongodb<T extends Mongodb<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private MongodbResources subresources = new MongodbResources();

	public Mongodb() {
		super();
        System.out.println("Mongodb created");
		this.key = "mongodb";
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
        System.out.println("Mongodb addPropertyChangeListener");
	}

	/**
	 * Removes a property change listener
	 */
	public void removePropertyChangeListener(
			java.beans.PropertyChangeListener listener) {
		if (this.pcs != null)
			this.pcs.removePropertyChangeListener(listener);
	}

	public MongodbResources subresources() {
		return this.subresources;
	}

	/**
	 * Add all Mongo objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Mongo objects.
	 */
	@SuppressWarnings("unchecked")
	public T mongos(java.util.List<Mongo> value) {
		this.subresources.mongos = value;
        System.out.println("Mongodb set list of Mongo");
		return (T) this;
	}

	/**
	 * Add the Mongo object to the list of subresources
	 * 
	 * @param value
	 *            The Mongo to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T mongo(Mongo value) {
		this.subresources.mongos.add(value);
        System.out.println("Mongodb set Mongo");
		return (T) this;
	}

	/**
	 * Create and configure a Mongo object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Mongo resource
	 * @param config
	 *            The MongoConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T mongo(java.lang.String childKey, MongoConsumer consumer) {
        System.out.println("Mongodb set child key with consumer");
		Mongo<? extends Mongo> child = new Mongo<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		mongo(child);
		return (T) this;
	}

	/**
	 * Create and configure a Mongo object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Mongo resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T mongo(java.lang.String childKey) {
        System.out.println("Mongodb set child key");
		mongo(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Mongo object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T mongo(MongoSupplier supplier) {
        System.out.println("Mongodb set supplier");
		mongo(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Mongodb
	 */
	public static class MongodbResources {
		/**
		 * MongoDB server definition
		 */
		@SubresourceInfo("mongo")
		private List<Mongo> mongos = new java.util.ArrayList<>();

		/**
		 * Get the list of Mongo resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Mongo> mongos() {
			return this.mongos;
		}

		public Mongo mongo(java.lang.String key) {
			return this.mongos.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}
}