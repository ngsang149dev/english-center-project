package com.abuenglishcenter.managementsystem.classroom;

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
@RequestMapping("/classes")
public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassScheduleService classScheduleService;

    public ClassroomController(ClassroomService classroomService, ClassScheduleService classScheduleService) {
        this.classroomService = classroomService;
        this.classScheduleService = classScheduleService;
    }

    @GetMapping
    public PageResponseDto<ClassroomResponseDto> getAllClassrooms(@RequestParam(defaultValue = "0") @Min(0) @Max(1000) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return classroomService.getAllClassrooms(page, size);
    }

    @GetMapping("/{id}/schedules")
    public List<ClassScheduleResponseDto> getSchedulesByClassroom(@PathVariable Long id) {
        return classScheduleService.getSchedulesByClassroom(id);
    }

    @PostMapping
    public ClassroomResponseDto createClassroom(@Valid @RequestBody ClassroomCreateRequestDto request) {
        return classroomService.createClassroom(request);
    }

    @PutMapping("/{id}/teacher")
    public ClassroomResponseDto updateTeacher(@PathVariable Long id,
            @Valid @RequestBody ClassroomTeacherUpdateRequestDto request) {
        return classroomService.updateTeacher(id, request.getTeacherId());
    }
}
