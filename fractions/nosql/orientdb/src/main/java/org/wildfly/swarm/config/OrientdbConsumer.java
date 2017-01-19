package org.wildfly.swarm.config;

import org.wildfly.swarm.config.Orientdb;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface OrientdbConsumer<T extends Orientdb<T>> {

	/**
	 * Configure a pre-constructed instance of Orientdb resource
	 * 
	 * @parameter Instance of Orientdb to configure
	 * @return nothing
	 */
	void accept(T value);

	default OrientdbConsumer<T> andThen(OrientdbConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}