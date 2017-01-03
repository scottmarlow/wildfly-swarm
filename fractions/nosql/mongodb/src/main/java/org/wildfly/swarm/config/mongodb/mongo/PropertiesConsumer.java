package org.wildfly.swarm.config.mongodb.mongo;

import org.wildfly.swarm.config.mongodb.mongo.Properties;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface PropertiesConsumer<T extends Properties<T>> {

	/**
	 * Configure a pre-constructed instance of Properties resource
	 * 
	 * @parameter Instance of Properties to configure
	 * @return nothing
	 */
	void accept(T value);

	default PropertiesConsumer<T> andThen(PropertiesConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}