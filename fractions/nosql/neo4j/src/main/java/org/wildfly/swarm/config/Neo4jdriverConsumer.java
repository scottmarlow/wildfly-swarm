package org.wildfly.swarm.config;

import org.wildfly.swarm.config.Neo4jdriver;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface Neo4jdriverConsumer<T extends Neo4jdriver<T>> {

	/**
	 * Configure a pre-constructed instance of Neo4jdriver resource
	 * 
	 * @parameter Instance of Neo4jdriver to configure
	 * @return nothing
	 */
	void accept(T value);

	default Neo4jdriverConsumer<T> andThen(Neo4jdriverConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}