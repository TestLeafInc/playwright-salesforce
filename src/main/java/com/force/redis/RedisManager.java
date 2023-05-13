package com.force.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class RedisManager {

	private Jedis jedis;

	public RedisManager() {
		this("localhost",6379);
	}
	
	public RedisManager(String host, int port) {
		this.jedis = new Jedis(host, port);
		//jedis.flushDB();
	}
	
	// Create only when it is not available before
	public void createTable(String dataStore) {
		jedis.sadd(dataStore, "");
	}

	// Get the count of records in the table for the queue column
	public Long getSetRecordCount(String dataStore) {
		return jedis.scard(dataStore);
	}

	// Get the count of records in the table for the queue column
	public Long getHashRecordCount(String dataStore) {
		return jedis.hlen(dataStore);
	}

	// Get all the records in the table
	public Set<String> getAllSetRecords(String dataStore) {
		return jedis.smembers(dataStore);
	}

	// Get all the records in the table
	public Map<String, String> getAllHashRecords(String dataStore) {
		return jedis.hgetAll(dataStore);
	}

	// Add only if the record does not exist
	public synchronized void pushToSet(String dataStore, String data) {
		jedis.sadd(dataStore, data);
	}

	// Remove an element from the Set and return it
	public synchronized String popFromSet(String dataStore) {
		return jedis.spop(dataStore);
	}

	// Get the random record in the table
	public String getRandomRecordFromHash(String dataStore) {

		// Get a random key from the hash
		Set<String> keys = jedis.hkeys(dataStore);
		return new ArrayList<>(keys).get((int) 
				(Math.random() * keys.size()));

	}

	// Get the top record in the table
	public String getUnusedRecordFromHash(String dataStore, String testName) {

		String key = null;

		// Get the keys from the hash
		List<String> keys = new ArrayList<>(jedis.hkeys(dataStore));

		// Iterator through the records
		for (int i = 0; i < keys.size(); i++) {

			// Get the key
			String nextKey = keys.get(i);

			// Get the value associated with the first key
			String value = jedis.hget(dataStore, nextKey);

			if(!value.contains(testName)) {
				jedis.hset(dataStore, nextKey, value+testName+",");
				value = jedis.hget(dataStore, nextKey);

				key = nextKey;
				break;
			}

		}

		return key;
	}

	// Add only if the record does not exist
	public synchronized void pushToHash(String dataStore, String data) {
		jedis.hset(dataStore, data, "");
	}

	// Remove an element from the Set and return it
	public synchronized String popFromHash(String dataStore) {

		// Get the first key from the hash
		Set<String> keys = jedis.hkeys(dataStore);
		String firstKey = keys.iterator().next();

		// Remove the first key-value pair from the hash
		jedis.hdel(dataStore, firstKey);

		return firstKey;
	}

	public void close() {
		jedis.close();
	}
}