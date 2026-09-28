package com.abuenglishcenter.managementsystem.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.Classroom;
import com.abuenglishcenter.managementsystem.classroom.ClassroomRepository;
import com.abuenglishcenter.managementsystem.teacher.Teacher;
import com.abuenglishcenter.managementsystem.teacher.TeacherRepository;

@Service 
public class TeacherRateService {

    @Autowired 
    private TeacherRateRepository teacherRateRepository;

    @Autowired 
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<TeacherRateResponseDto> getAllTeacherRates() {
        return teacherRateRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional 
    public TeacherRateResponseDto createTeacherRate(TeacherRateCreateRequestDto request) {
        Teacher checkTeacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Classroom checkClassroom = classroomRepository.findById(request.getClassroomId()).orElseThrow(() -> new RuntimeException("Classroom not found"));

        closeCurrentActiveRate(checkTeacher.getId(), checkClassroom.getId(), request.getEffectiveFrom());

        TeacherRate newRate = new TeacherRate();
        newRate.setTeacher(checkTeacher);
        newRate.setClassroom(checkClassroom);
        newRate.setRatePerSession(request.getRatePerSession());
        newRate.setEffectiveFrom(request.getEffectiveFrom());

        TeacherRate saved = teacherRateRepository.save(newRate);
        return toDto(saved);
    }

    private void closeCurrentActiveRate(Long teacherId, Long classroomId, LocalDate newEffectiveFrom) {
        Optional<TeacherRate> currentActiveRate = teacherRateRepository.findByTeacherIdAndClassroomIdAndEffectiveToIsNull(teacherId, classroomId);

        if (currentActiveRate.isPresent()) {
            TeacherRate oldRate = currentActiveRate.get();
            oldRate.setEffectiveTo(newEffectiveFrom.minusDays(1));
            teacherRateRepository.save(oldRate);
        }
    }

    private TeacherRateResponseDto toDto(TeacherRate teacherRate) {
        return new TeacherRateResponseDto(teacherRate.getId(), teacherRate.getTeacher().getId(), teacherRate.getClassroom().getId(), teacherRate.getRatePerSession(), teacherRate.getEffectiveFrom(), teacherRate.getEffectiveTo());
    } 
}
