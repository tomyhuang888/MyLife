package com.th.mylife;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.*;

import java.lang.*;
// import java.util.Calendar; //for calendar


//create test cases for:
//      accessing different timezones
//      
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalendarQuickstart {
    private final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";


    public List<Event> getEvents(DateTime beg, DateTime end) throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar.Builder cb = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT));
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Events events = service.events().list("primary")
                // .setMaxResults(10)
                .setTimeMin(beg)
                .setTimeMax(end)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            return items;

        }
        return null;
    }

    @GetMapping("/calendar")
    public String getCalendarWeek(Model model){
    // public static void getWeekEvents(){
        if(Desktop.isDesktopSupported()){
            System.out.println("DESKTOP SUPPORTED");
        }else{

            System.out.println("DESKTOP NOT SUPPORTED");
        }
        long currentTime = System.currentTimeMillis();//-2*dayInMillis;
        CalendarRange calRange;
        try{
             calRange = new CalendarRange(currentTime+CalendarRange.DAY_IN_MILLIS*7);
        }catch(Exception e){
            return "calendar";
        }

        DateTime ctDT = new DateTime(currentTime);
        System.out.println(ctDT.toString());
        DateTime sodDT = new DateTime(calRange.getBeginningOfFirstDay());
        System.out.println(sodDT.toString());
        DateTime eodDT = new DateTime(calRange.getEndOfLastDay());
        System.out.println(eodDT.toString());
        // DateTime weekFromNow = new DateTime(currentTime + 7*dayInMillis);

        // List the next 10 events from the primary calendar.
        try{
            model.addAttribute("events", getEvents(sodDT, eodDT));
        }catch(Exception e){
            
        }

        return "calendar";
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Calendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receier = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receier).authorize("user");
    }
}