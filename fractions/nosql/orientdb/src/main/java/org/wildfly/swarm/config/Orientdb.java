package org.wildfly.swarm.config;

import org.wildfly.swarm.config.runtime.Address;
import java.util.HashMap;
import org.wildfly.swarm.config.runtime.ResourceType;
import org.wildfly.swarm.config.runtime.Implicit;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.wildfly.swarm.config.runtime.Subresource;
import org.wildfly.swarm.config.orientdb.OrientConsumer;
import org.wildfly.swarm.config.orientdb.OrientSupplier;
import org.wildfly.swarm.config.orientdb.Orient;
import org.wildfly.swarm.config.runtime.SubresourceInfo;
import org.wildfly.swarm.config.runtime.ModelNodeBinding;

/**
 * OrientDB driver subsystem
 */
@Address("/subsystem=orientdb")
@ResourceType("subsystem")
@Implicit
public class Orientdb<T extends Orientdb<T>> extends HashMap
		implements
			org.wildfly.swarm.config.runtime.Keyed {

	private String key;
	private PropertyChangeSupport pcs;
	private OrientdbResources subresources = new OrientdbResources();

       public Orientdb(final String key) {
                super();
                this.key = key;
                this.pcs = new PropertyChangeSupport(this);
        }

	public Orientdb() {
		super();
		this.key = "orientdb";
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

	public OrientdbResources subresources() {
		return this.subresources;
	}

	/**
	 * Add all Orient objects to this subresource
	 * 
	 * @return this
	 * @param value
	 *            List of Orient objects.
	 */
	@SuppressWarnings("unchecked")
	public T orients(java.util.List<Orient> value) {
		this.subresources.orients = value;
		return (T) this;
	}

	/**
	 * Add the Orient object to the list of subresources
	 * 
	 * @param value
	 *            The Orient to add
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T orient(Orient value) {
		this.subresources.orients.add(value);
		return (T) this;
	}

	/**
	 * Create and configure a Orient object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Orient resource
	 * @param config
	 *            The OrientConsumer to use
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T orient(java.lang.String childKey, OrientConsumer consumer) {
		Orient<? extends Orient> child = new Orient<>(childKey);
		if (consumer != null) {
			consumer.accept(child);
		}
		orient(child);
		return (T) this;
	}

	/**
	 * Create and configure a Orient object to the list of subresources
	 * 
	 * @param key
	 *            The key for the Orient resource
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T orient(java.lang.String childKey) {
		orient(childKey, null);
		return (T) this;
	}

	/**
	 * Install a supplied Orient object to the list of subresources
	 */
	@SuppressWarnings("unchecked")
	public T orient(OrientSupplier supplier) {
		orient(supplier.get());
		return (T) this;
	}

	/**
	 * Child mutators for Orientdb
	 */
	public static class OrientdbResources {
		/**
		 * OrientDB server definition
		 */
		@SubresourceInfo("orient")
		private List<Orient> orients = new java.util.ArrayList<>();

		/**
		 * Get the list of Orient resources
		 * 
		 * @return the list of resources
		 */
		@Subresource
		public List<Orient> orients() {
			return this.orients;
		}

		public Orient orient(java.lang.String key) {
			return this.orients.stream().filter(e -> e.getKey().equals(key))
					.findFirst().orElse(null);
		}
	}
}
