package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired 
    private ClassroomService classroomService;

    @Autowired 
    private ClassScheduleService classScheduleService;

    @GetMapping 
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/{id}/schedules")
    public List<ClassScheduleResponseDto> getSchedulesByClassroom(@PathVariable Long id) {
        return classScheduleService.getSchedulesByClassroom(id);
    }

    @PostMapping
    public ClassroomResponseDto createClassroom(@RequestBody ClassroomCreateRequestDto request) {
        return classroomService.createClassroom(request);
    }

    @PutMapping("/{id}/teacher") 
    public ClassroomResponseDto updateTeacher(@PathVariable Long id, @RequestBody Long teacherId) {
        return classroomService.updateTeacher(id, teacherId);
    }
}
