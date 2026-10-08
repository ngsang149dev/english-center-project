package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomService.getAllClassrooms();
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
    public ClassroomResponseDto updateTeacher(@PathVariable Long id, @RequestBody Long teacherId) {
        return classroomService.updateTeacher(id, teacherId);
    }
}
