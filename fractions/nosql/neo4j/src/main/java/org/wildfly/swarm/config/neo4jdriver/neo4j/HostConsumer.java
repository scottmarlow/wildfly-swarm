package org.wildfly.swarm.config.neo4jdriver.neo4j;

import org.wildfly.swarm.config.neo4jdriver.neo4j.Host;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface HostConsumer<T extends Host<T>> {

	/**
	 * Configure a pre-constructed instance of Host resource
	 * 
	 * @parameter Instance of Host to configure
	 * @return nothing
	 */
	void accept(T value);

	default HostConsumer<T> andThen(HostConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}