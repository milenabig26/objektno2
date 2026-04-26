package org.acme;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "time_records") 
public class TimeResponseRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_record_seq")
    @SequenceGenerator(name = "time_record_seq", sequenceName = "time_record_seq", allocationSize = 1)
    public Long id; 

    public String dateTime;
    public String timeZone;
    public int year;
    public int month;
    public int day;
    public String time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public Users user;

    public TimeResponseRecord() {}
}