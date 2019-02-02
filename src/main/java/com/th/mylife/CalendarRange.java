package com.th.mylife;

import java.util.*;
import java.time.*;

public class CalendarRange{

	public static int DAY_IN_MILLIS = 1000*60*60*24;

	private long startMillis;
	private long endMillis;

	private int tzShiftInSeconds;

	public CalendarRange() throws InvalidCalendarRangeException{
		this(System.currentTimeMillis(), System.currentTimeMillis()+1);
	}

	public CalendarRange(long endMillis) throws InvalidCalendarRangeException{
		this(System.currentTimeMillis(), endMillis);
	}

	public CalendarRange(long startMillis, long endMillis) throws InvalidCalendarRangeException{
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		if(!isValidRange()){
			throw new InvalidCalendarRangeException("endMillis is not greater than startMillis");
		}
		Instant instant = Instant.ofEpochMilli(startMillis);
        ZoneId systemZone = ZoneId.systemDefault(); // get my current timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);
        this.tzShiftInSeconds = currentOffsetForMyZone.getTotalSeconds();
        System.out.println(this.tzShiftInSeconds);
	}

	public long getTimeZoneShiftInSeconds(){
		return this.tzShiftInSeconds;
	}

	public long getTimeZoneShiftInMillis(){
		return (long) this.tzShiftInSeconds*1000;
	}

	//returns Beginning Of First Day in Milliseconds.
	public long getBeginningOfFirstDay(){
		if(tzShiftInSeconds < 0){
			return startMillis - (startMillis % DAY_IN_MILLIS) - (tzShiftInSeconds*1000);
		}else{
			return startMillis - (startMillis % DAY_IN_MILLIS) + (tzShiftInSeconds*1000);
		}
	}

	//returns End of Last DAy in Milliseconds.
	public long getEndOfLastDay(){
		if(tzShiftInSeconds < 0){
			return endMillis + DAY_IN_MILLIS - (endMillis % DAY_IN_MILLIS) - (tzShiftInSeconds*1000);
		}else{
			return endMillis + DAY_IN_MILLIS - (endMillis % DAY_IN_MILLIS) + (tzShiftInSeconds*1000);
		}
	}

	private boolean isValidRange(){
		return this.endMillis > this.startMillis;
	}


}