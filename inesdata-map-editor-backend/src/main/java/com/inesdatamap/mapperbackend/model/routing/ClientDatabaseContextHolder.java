package com.inesdatamap.mapperbackend.model.routing;

/**
 * Stores the current context as a {@link ThreadLocal} reference.
 */
public final class ClientDatabaseContextHolder {

	private static final ThreadLocal<Long> contextHolder = new ThreadLocal<>();

	/**
	 * Private constructor
	 */
	private ClientDatabaseContextHolder() {
	}

	/**
	 * Set the client database
	 *
	 * @param clientDatabase
	 * 	the client database
	 */
	public static void set(Long clientDatabase) {
		if (clientDatabase == null) {
			throw new IllegalArgumentException("clientDatabase cannot be null");
		}
		contextHolder.set(clientDatabase);
	}

	/**
	 * Get the client database
	 *
	 * @return the client database
	 */
	public static Long get() {
		return contextHolder.get();
	}

	/**
	 * Clear the client database
	 */
	public static void clear() {
		contextHolder.remove();
	}
}
