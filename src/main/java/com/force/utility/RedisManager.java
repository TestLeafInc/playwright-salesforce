package com.force.utility;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisManager {

	private Jedis jedis;

	public RedisManager(String host, int port) {
		this.jedis = new Jedis(host, port);
		//jedis.flushDB();
	}

	// Create only when it is not available before
	public void createTable(String tableName) {
		//jedis.sadd(tableName, "");
	}

	// Get the count of records in the table for the queue column
	public Long getRecordCount(String tableName) {
		return jedis.scard(tableName);
	}
	
	// Get all the records in the table
	public Set<String> getAllRecords(String tableName) {
		return jedis.smembers(tableName);
	}

	// Add only if the record does not exist
	public synchronized void pushToTable(String tableName, String data) {
		jedis.sadd(tableName, data);
	}

	// Remove an element from the Set and return it
	public synchronized String popFromTable(String tableName) {
		return jedis.spop(tableName);
	}

	public void close() {
		jedis.close();
	}
}