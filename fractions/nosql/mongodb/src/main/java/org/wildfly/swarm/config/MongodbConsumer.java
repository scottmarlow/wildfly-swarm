package org.wildfly.swarm.config;

import org.wildfly.swarm.config.Mongodb;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface MongodbConsumer<T extends Mongodb<T>> {

	/**
	 * Configure a pre-constructed instance of Mongodb resource
	 * 
	 * @parameter Instance of Mongodb to configure
	 * @return nothing
	 */
	void accept(T value);

	default MongodbConsumer<T> andThen(MongodbConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}