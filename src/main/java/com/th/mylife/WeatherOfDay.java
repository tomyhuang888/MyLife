package com.th.mylife;
import java.net.*;
import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class WeatherOfDay {
	private String dayOfWeek;
	private String date;
	private String description;
	private int highestTemp;
	private int lowestTemp;
	private int precipitation;
	private String wind;
	private int humidity;

	private Map<String, String> properties;

	public WeatherOfDay(){
		properties = new HashMap<String, String>();
	}

	public WeatherOfDay[] getTenDayWeather() throws Exception{
		WeatherOfDay[] wods = new WeatherOfDay[11];
		Document doc = Jsoup.connect("https://weather.com/weather/tenday/l/Santa+Ana+CA+USCA1016:1:US").get();
		System.out.println(doc.title());
		Elements newsHeadlines = doc.select(".twc-table .clickable");
		int count = 0;
		for (Element headline : newsHeadlines) {
			Elements trData = headline.children();
			WeatherOfDay wod = new WeatherOfDay();
			for(Element data : trData){
				wod.setNewProperty(data);
				// System.out.println(String.format("%s = %s", attr.get("headers"), data.text()));
			}
			wod.printProperties();
			wods[count] = wod;
			System.out.println(count);
			count++;
		}
		return wods;
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