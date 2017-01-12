package org.wildfly.swarm.config.cassandradriver;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface CassandraSupplier<T extends Cassandra> {

	/**
	 * Constructed instance of Cassandra resource
	 * 
	 * @return The instance
	 */
	public Cassandra get();
}