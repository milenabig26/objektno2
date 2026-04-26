package org.acme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeResponse {
    public String dateTime;
    public String timeZone;
    public int year;
    public int month;
    public int day;
    public String time;
}