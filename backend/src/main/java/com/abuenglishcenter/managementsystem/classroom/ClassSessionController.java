package com.abuenglishcenter.managementsystem.classroom;

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
@RequestMapping("/class-sessions")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @GetMapping
    public PageResponseDto<ClassSessionResponseDto> getAllClassSessions(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return classSessionService.getAllClassSessions(page, size);
    }

    @PostMapping
    public ClassSessionResponseDto createClassSession(@Valid @RequestBody ClassSessionCreateRequestDto request) {
        return classSessionService.createClassSession(request);
    }

    @PutMapping("/{id}/teacher")
    public ClassSessionResponseDto updateTeacher(@PathVariable Long id,
            @Valid @RequestBody ClassSessionTeacherUpdateRequestDto request) {
        return classSessionService.updateTeacher(id, request.getTeacherId());
    }

    @PutMapping("/{id}")
    public ClassSessionResponseDto updateIsTaught(@PathVariable Long id,
            @Valid @RequestBody ClassSessionUpdateRequestDto request) {
        return classSessionService.updateIsTaught(id, request.getTeacherTaught());
    }
}
