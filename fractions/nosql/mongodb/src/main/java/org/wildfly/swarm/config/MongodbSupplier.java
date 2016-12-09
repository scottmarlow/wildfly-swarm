package org.wildfly.swarm.config;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface MongodbSupplier<T extends Mongodb> {

	/**
	 * Constructed instance of Mongodb resource
	 * 
	 * @return The instance
	 */
	public Mongodb get();
}