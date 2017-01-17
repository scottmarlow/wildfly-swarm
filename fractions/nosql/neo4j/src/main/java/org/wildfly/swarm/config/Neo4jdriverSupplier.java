package org.wildfly.swarm.config;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface Neo4jdriverSupplier<T extends Neo4jdriver> {

	/**
	 * Constructed instance of Neo4jdriver resource
	 * 
	 * @return The instance
	 */
	public Neo4jdriver get();
}