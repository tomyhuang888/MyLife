package com.th.mylife;

public class InvalidCalendarRangeException extends Exception{
	
	public InvalidCalendarRangeException(String errorMessage){
		super(errorMessage);
	}
}