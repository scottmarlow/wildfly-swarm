package org.wildfly.swarm.config.cassandradriver;

import org.wildfly.swarm.config.cassandradriver.Cassandra;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface CassandraConsumer<T extends Cassandra<T>> {

	/**
	 * Configure a pre-constructed instance of Cassandra resource
	 * 
	 * @parameter Instance of Cassandra to configure
	 * @return nothing
	 */
	void accept(T value);

	default CassandraConsumer<T> andThen(CassandraConsumer<T> after) {
		return (c) -> {
			this.accept(c);
			after.accept(c);
		};
	}
}