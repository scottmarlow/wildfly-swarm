package org.wildfly.swarm.config.orientdb.orient;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface HostSupplier<T extends Host> {

	/**
	 * Constructed instance of Host resource
	 * 
	 * @return The instance
	 */
	public Host get();
}