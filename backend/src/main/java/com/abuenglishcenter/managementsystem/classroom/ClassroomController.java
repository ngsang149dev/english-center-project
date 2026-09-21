package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/classes")
public class ClassroomController {

    @Autowired 
    private ClassroomService classroomService;

    @GetMapping 
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @PostMapping
    public ClassroomResponseDto createClassroom(@RequestBody ClassroomCreateRequestDto request) {
        return classroomService.createClassroom(request);
    }
}
