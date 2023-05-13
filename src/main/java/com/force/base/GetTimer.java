package com.force.base;

import java.time.LocalDateTime;

public class GetTimer {

	public static void main(String[] args) {
		GetTimer timer = new GetTimer();
		
		int nextHourSlot = timer.getNextHourSlot();
		System.out.println(nextHourSlot);

		int nextMinSlot = timer.getNextMinuteSlot();
		System.out.println(nextMinSlot);
		
		String nextSlot = timer.getNextSlot();
		System.out.println(nextSlot);
		
		System.out.println(timer.getCurrentHour());
		System.out.println(timer.getCurrentMinute());
		
		timer.sleepUntilNextSlot(nextHourSlot,nextMinSlot);
		
		System.out.println(timer.getCurrentHour());
		System.out.println(timer.getCurrentMinute());
		
	}

	public int getNextHourSlot() {
        return getNextSlot("hour");
	}

	public int getNextMinuteSlot() {
		return getNextSlot("minute");
	}
	
	public String getNextSlot() {
		return getNextSlot("hour")+":"+getNextSlot("minute");
	}
	
	public int getCurrentHour() {
        return LocalDateTime.now().getHour();
	}

	public int getCurrentMinute() {
		return LocalDateTime.now().getMinute();
	}
	
	public void sleepUntilNextSlot(int hour, int minute) {
		
		while(getCurrentHour() <= hour && getCurrentMinute() < minute) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private int getNextSlot(String type) {
		 // get current time
        LocalDateTime now = LocalDateTime.now();

        // add 6 minutes
        LocalDateTime plusSixMinutes = now.plusMinutes(2);

        // find the next minute that can be divided by 5
        int nextMinute = plusSixMinutes.getMinute();
        while (nextMinute % 5 != 0) {
            nextMinute++;
        }
        
        LocalDateTime nextMinuteDivisibleByFive = plusSixMinutes.withMinute(nextMinute);
        if(type.equals("hour"))
        	return nextMinuteDivisibleByFive.getHour();
        else
        	return nextMinuteDivisibleByFive.getMinute();

	}
}
