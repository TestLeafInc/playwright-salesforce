package com.force.utility;

import org.testng.annotations.Test;

public class ReadDataFromRedis {

	@Test
	public void redisTests() {
		
		// Replace "localhost" and 6379 with Redis server's IP and port, if different.
		RedisManager redisManager = new RedisManager("20.244.5.250", 6379);
		
				
		Long recordCount = redisManager.getRecordCount("registrations");
		System.out.println(recordCount);
		
		// Pop data from table
		//System.out.println(redisManager.popFromTable(tableName));
		//System.out.println(redisManager.getAllRecords(tableName));

		
		// Push data to another table
		//redisManager.pushToTable("admission", "APJ1.0003717579");
		//System.out.println(redisManager.getRecordCount("admission"));

		redisManager.close();
	}
}
