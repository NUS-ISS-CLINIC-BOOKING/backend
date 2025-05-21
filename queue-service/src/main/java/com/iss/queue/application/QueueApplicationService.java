package com.iss.queue.application;

import com.iss.queue.domain.entity.Doctor;
import com.iss.queue.dto.GetDoctorQueueResult;
import com.iss.queue.dto.GetDoctorsResult;
import com.iss.queue.repository.DoctorRepository;
import com.iss.queue.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.iss.queue.domain.entity.Slot;
@Service
@RequiredArgsConstructor
public class QueueApplicationService {
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;
    public final String[] TIME_SLOTS = {
            "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00",
            "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00"
    };
    public GetDoctorsResult getDoctorsByClinicId(int clinicId) {
        try {
            List<Doctor> doctors = doctorRepository.getDoctorsByClinicId(clinicId);
            if(doctors.isEmpty()) {
                return new GetDoctorsResult(new ArrayList<>(), "No doctors found");
            } else {
                return new GetDoctorsResult(doctors, "get doctors successfully");
            }
        } catch (Exception e) {
            return new GetDoctorsResult(null, e.getMessage());
        }
    }

    /*
        date: 2024-11-03
     */
    public GetDoctorQueueResult getDoctorQueueByDoctorId(Long doctorId, String date) {
        List<Slot> slots = new ArrayList<>();
        String doctorName, specialty;
        try {
            doctorName = doctorRepository.getDoctorNameById(doctorId);
            specialty = doctorRepository.getSpecialtyById(doctorId);
        } catch (Exception e) {
            return new GetDoctorQueueResult(null, null, "get doctor name failed");
        }

        for(String time : TIME_SLOTS) {
            Long patientId = slotRepository.getPatientIdBySlotTime(time, doctorId, date);
            if(patientId != null) {
                slots.add(new Slot(date, time, patientId, false));
            } else {
                slots.add(new Slot(date, time, null, true));
            }
        }
        return new GetDoctorQueueResult(new Doctor(doctorId, doctorName, specialty), slots, "get doctor queue successfully");
    }

    public GetDoctorsResult getDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = new ArrayList<>();
        try {
            doctors = doctorRepository.getDoctorsBySpecialty(specialty);
            if(doctors.isEmpty()) {
                return new GetDoctorsResult(doctors, "No doctors found");
            }
            return new GetDoctorsResult(doctors, "get doctors successfully");
        } catch (Exception e) {
            return new GetDoctorsResult(doctors, "getdoctorsbyspecialty failed" + e.getMessage());
        }
    }
}
