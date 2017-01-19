package org.wildfly.swarm.config.orientdb;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface OrientSupplier<T extends Orient> {

	/**
	 * Constructed instance of Orient resource
	 * 
	 * @return The instance
	 */
	public Orient get();
}