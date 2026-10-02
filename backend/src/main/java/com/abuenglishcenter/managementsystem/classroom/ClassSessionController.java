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
@RequestMapping("/class-sessions")
public class ClassSessionController {

    @Autowired 
    private ClassSessionService classSessionService;

    @GetMapping 
    public List<ClassSessionResponseDto> getAllClassSessions() {
        return classSessionService.getAllClassSessions();

    }

    @PostMapping 
    public ClassSessionResponseDto createClassSession(@RequestBody ClassSessionCreateRequestDto request) {
        return classSessionService.createClassSession(request);
    }

    @PutMapping("{id}/teacher") 
    public ClassSessionResponseDto updateTeacher(@PathVariable Long id, @RequestBody Long newTeacherId) {
        return classSessionService.updateTeacher(id, newTeacherId);
    }

    @PutMapping("/{id}")
    public ClassSessionResponseDto updateIsTaught(@PathVariable Long id, @RequestBody boolean isTaught) {
        return classSessionService.updateIsTaught(id, isTaught);
    }
}
