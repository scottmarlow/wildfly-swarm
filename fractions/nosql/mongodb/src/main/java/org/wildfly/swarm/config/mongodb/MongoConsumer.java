package org.wildfly.swarm.config.mongodb;

import org.wildfly.swarm.config.mongodb.Mongo;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface MongoConsumer<T extends Mongo<T>> {

	/**
	 * Configure a pre-constructed instance of Mongo resource
	 * 
	 * @parameter Instance of Mongo to configure
	 * @return nothing
	 */
	void accept(T value);

	default MongoConsumer<T> andThen(MongoConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}