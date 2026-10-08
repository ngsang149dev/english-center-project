package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class-schedules")
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    @GetMapping
    public List<ClassScheduleResponseDto> getALlClassSchedules() {
        return classScheduleService.getAllClassSchedules();
    }

    @PostMapping
    public ClassScheduleResponseDto createClassSchedule(@RequestBody ClassScheduleCreateRequestDto request) {
        return classScheduleService.createSchedule(request);
    }
}
