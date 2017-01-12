package org.wildfly.swarm.config;

import org.wildfly.swarm.config.Cassandradriver;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface CassandradriverConsumer<T extends Cassandradriver<T>> {

	/**
	 * Configure a pre-constructed instance of Cassandradriver resource
	 * 
	 * @parameter Instance of Cassandradriver to configure
	 * @return nothing
	 */
	void accept(T value);

	default CassandradriverConsumer<T> andThen(CassandradriverConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}