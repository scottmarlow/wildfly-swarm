package org.wildfly.swarm.config.orientdb;

import org.wildfly.swarm.config.orientdb.Orient;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface OrientConsumer<T extends Orient<T>> {

	/**
	 * Configure a pre-constructed instance of Orient resource
	 * 
	 * @parameter Instance of Orient to configure
	 * @return nothing
	 */
	void accept(T value);

	default OrientConsumer<T> andThen(OrientConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}