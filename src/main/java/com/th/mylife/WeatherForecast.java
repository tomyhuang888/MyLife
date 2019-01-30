package com.th.mylife;

import java.net.*;
import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
public class WeatherForecast{
	public WeatherForecast(){

	}

	public ArrayList<WeatherData> getWeekWeather() throws Exception{
		ArrayList<WeatherData> wods = new ArrayList<WeatherData>();
		Document doc = Jsoup.connect("https://weather.com/weather/tenday/l/Santa+Ana+CA+USCA1016:1:US").get();
		System.out.println(doc.title());
		Elements newsHeadlines = doc.select(".twc-table .clickable");
		for (Element headline : newsHeadlines) {
			if(wods.size() == 7){
				break;
			}
			// System.out.println(String.format("array.length = %d", wods.length));
			Elements trData = headline.children();
			WeatherData wod = new WeatherData();
			for(Element data : trData){
				wod.setNewProperty(data);
			}
			wods.add(wod);
		}
		return wods;
	}
}