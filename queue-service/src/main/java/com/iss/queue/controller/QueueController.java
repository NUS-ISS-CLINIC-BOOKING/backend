package com.iss.queue.controller;

import com.iss.queue.application.QueueApplicationService;
import com.iss.queue.common.response.SuccessResponse;
import com.iss.queue.dto.BookSlotResult;
import com.iss.queue.dto.GetDoctorQueueResult;
import com.iss.queue.dto.GetDoctorsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {
    private final QueueApplicationService clinicApplicationService;

    @GetMapping("/clinicDoctor/{clinicId}")
    public SuccessResponse<GetDoctorsResult> getDoctorsByClinicId(@PathVariable int clinicId) {
        return new SuccessResponse<>(clinicApplicationService.getDoctorsByClinicId(clinicId));
    }

    @GetMapping("/getDoctorQueue/{doctorId}/{date}")
    public SuccessResponse<GetDoctorQueueResult> getDoctorQueue(
            @PathVariable Long doctorId,
            @PathVariable String date) {

        return new SuccessResponse<>(
                clinicApplicationService.getDoctorQueueByDoctorId(doctorId, date)
        );
    }

    @GetMapping("/specialtyDoctor/{specialty}")
    public SuccessResponse<GetDoctorsResult> getDoctorsBySpecialtyId(@PathVariable String specialty) {
        return new SuccessResponse<>(clinicApplicationService.getDoctorsBySpecialty(specialty));
    }

    @GetMapping("/clinicSpecialtyDoctor/{clinicId}/{specialty}")
    public SuccessResponse<GetDoctorsResult> getDoctorsByClinicIdAndSpecialtyId(
            @PathVariable int clinicId,
            @PathVariable String specialty) {
        return new SuccessResponse<>(clinicApplicationService.getDoctorsByClinicIdAndSpecialty(clinicId, specialty));
    }

    @PutMapping("/bookSlot/{date}/{slotId}/{clinicId}/{doctorId}/{patientId}")
    public SuccessResponse<BookSlotResult> bookSlot(
            @PathVariable String date,
            @PathVariable int slotId, @PathVariable int clinicId, @PathVariable Long doctorId, @PathVariable Long patientId) {
        return new SuccessResponse<>(clinicApplicationService.bookSlot(date, slotId, clinicId, doctorId, patientId));
    }
}