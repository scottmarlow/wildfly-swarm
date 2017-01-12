package org.wildfly.swarm.config.cassandradriver.cassandra;

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