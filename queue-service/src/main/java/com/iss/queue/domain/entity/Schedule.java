package com.iss.queue.domain.entity;
import lombok.Getter;


@Getter
public class Schedule {
    private Long id;
    private int clinicId;
    private int doctorId;
    private String date;
    private String startTime;
    private String endTime;

    public Schedule(Long id, int clinicId, int doctorId, String date, String startTime, String endTime) {
        this.id = id;
        this.clinicId = clinicId;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}