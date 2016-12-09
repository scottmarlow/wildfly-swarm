package org.wildfly.swarm.config.mongodb.mongo;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface PropertiesSupplier<T extends Properties> {

	/**
	 * Constructed instance of Properties resource
	 * 
	 * @return The instance
	 */
	public Properties get();
}