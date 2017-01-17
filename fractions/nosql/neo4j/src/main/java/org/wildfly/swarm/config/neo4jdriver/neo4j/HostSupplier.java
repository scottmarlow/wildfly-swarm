package org.wildfly.swarm.config.neo4jdriver.neo4j;

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