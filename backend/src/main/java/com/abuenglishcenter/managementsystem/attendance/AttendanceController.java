package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public PageResponseDto<AttendanceResponseDto> getAllAttendances(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long enrollmentId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return attendanceService.getAttendances(sessionId, enrollmentId, page, size);
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
