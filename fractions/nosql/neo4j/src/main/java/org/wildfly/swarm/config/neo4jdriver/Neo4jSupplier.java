package org.wildfly.swarm.config.neo4jdriver;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface Neo4jSupplier<T extends Neo4j> {

	/**
	 * Constructed instance of Neo4j resource
	 * 
	 * @return The instance
	 */
	public Neo4j get();
}