package org.wildfly.swarm.config;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface OrientdbSupplier<T extends Orientdb> {

	/**
	 * Constructed instance of Orientdb resource
	 * 
	 * @return The instance
	 */
	public Orientdb get();
}