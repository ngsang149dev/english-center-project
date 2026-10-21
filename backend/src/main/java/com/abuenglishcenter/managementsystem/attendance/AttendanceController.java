package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<AttendanceResponseDto> getAllAttendances(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long enrollmentId) {
        return attendanceService.getAttendances(sessionId, enrollmentId);
    }

    @PostMapping
    public List<AttendanceResponseDto> createAttendance(@Valid @RequestBody AttendanceCreateRequestDto request) {
        return attendanceService.createAttendance(request);
    }

    @PutMapping("/{id}")
    public AttendanceResponseDto updateAttendance(@PathVariable Long id,
            @Valid @RequestBody AttendanceUpdateRequestDto request) {
        return attendanceService.updateAttendance(id, request);
    }
}
