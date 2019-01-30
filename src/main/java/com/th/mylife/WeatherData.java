package com.th.mylife;
import java.net.*;
import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class WeatherData {
	// private String dayOfWeek;
	// private String date;
	// private String description;
	// private int highestTemp;
	// private int lowestTemp;
	// private int precipitation;
	// private String wind;
	// private int humidity;

	private Map<String, String> properties;

	public WeatherData(){
		properties = new HashMap<String, String>();
	}

	public void setNewProperty(Element data){
		Attributes attr = data.attributes();
		if(attr.hasKey("headers")){
			if (attr.get("headers").contentEquals("day")){
				String day = data.text();
				int index = day.indexOf(" ");
				String date_time = day.substring(0, index);
				String day_detail = day.substring(index, day.length());
				this.properties.put("date_time", date_time);
				this.properties.put("day_detail", day_detail);
			} else if (attr.get("headers").contentEquals("hi-lo")){
				String hi = data.child(0).child(0).text();
				String lo = data.child(0).child(2).text();
				this.properties.put("hi", hi);
				this.properties.put("lo", lo);
			} else{
				this.properties.put(attr.get("headers"), data.text());
			}
		}
	}

	public Map<String, String> getProperties(){
		return this.properties;
	}

	public void printProperties(){
		Iterator<String> keyIter = this.properties.keySet().iterator();
		while(keyIter.hasNext()){
			String key = keyIter.next();
			System.out.println(String.format("%s: %s", key, this.properties.get(key)));
		}
		System.out.println("\n\n\n");
	}
}