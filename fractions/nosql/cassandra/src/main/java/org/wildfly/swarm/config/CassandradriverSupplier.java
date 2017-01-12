package org.wildfly.swarm.config;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface CassandradriverSupplier<T extends Cassandradriver> {

	/**
	 * Constructed instance of Cassandradriver resource
	 * 
	 * @return The instance
	 */
	public Cassandradriver get();
}