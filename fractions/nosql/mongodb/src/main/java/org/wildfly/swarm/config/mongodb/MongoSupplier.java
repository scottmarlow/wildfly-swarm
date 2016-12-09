package org.wildfly.swarm.config.mongodb;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface MongoSupplier<T extends Mongo> {

	/**
	 * Constructed instance of Mongo resource
	 * 
	 * @return The instance
	 */
	public Mongo get();
}