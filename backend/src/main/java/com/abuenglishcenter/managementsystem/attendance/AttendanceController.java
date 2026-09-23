package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired 
    private AttendanceService attendanceService;

    @GetMapping 
    public List<AttendanceResponseDto> getAllAttendances() {
        return attendanceService.getAllAttendances();
    }

    @PostMapping 
    public List<AttendanceResponseDto> createAttendance(@RequestBody AttendanceCreateRequestDto request) {
        return attendanceService.createAttendance(request);
    }
}
