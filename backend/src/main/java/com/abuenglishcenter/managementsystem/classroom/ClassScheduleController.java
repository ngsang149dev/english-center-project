package com.abuenglishcenter.managementsystem.classroom;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController
@RequestMapping("/class-schedules")
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    @GetMapping
    public PageResponseDto<ClassScheduleResponseDto> getALlClassSchedules(@RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return classScheduleService.getAllClassSchedules(page, size);
    }

    @PostMapping
    public ClassScheduleResponseDto createClassSchedule(@Valid @RequestBody ClassScheduleCreateRequestDto request) {
        return classScheduleService.createSchedule(request);
    }
}
