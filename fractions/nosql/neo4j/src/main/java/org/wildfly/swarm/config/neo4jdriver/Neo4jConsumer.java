package org.wildfly.swarm.config.neo4jdriver;

import org.wildfly.swarm.config.neo4jdriver.Neo4j;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface Neo4jConsumer<T extends Neo4j<T>> {

	/**
	 * Configure a pre-constructed instance of Neo4j resource
	 * 
	 * @parameter Instance of Neo4j to configure
	 * @return nothing
	 */
	void accept(T value);

	default Neo4jConsumer<T> andThen(Neo4jConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}