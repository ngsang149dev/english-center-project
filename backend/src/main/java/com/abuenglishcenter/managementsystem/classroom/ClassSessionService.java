package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.payroll.PayrollStatus;
import com.abuenglishcenter.managementsystem.payroll.TeacherPayrollRepository;
import com.abuenglishcenter.managementsystem.teacher.Teacher;
import com.abuenglishcenter.managementsystem.teacher.TeacherRepository;

@Service
public class ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherPayrollRepository teacherPayrollRepository;

    public ClassSessionService(ClassSessionRepository classSessionRepository, ClassroomRepository classroomRepository,
            TeacherRepository teacherRepository, TeacherPayrollRepository teacherPayrollRepository) {
        this.classSessionRepository = classSessionRepository;
        this.classroomRepository = classroomRepository;
        this.teacherRepository = teacherRepository;
        this.teacherPayrollRepository = teacherPayrollRepository;
    }

    public List<ClassSessionResponseDto> getAllClassSessions() {
        return classSessionRepository.findAll().stream().map(this::toDto).toList();
    }

    public ClassSessionResponseDto createClassSession(ClassSessionCreateRequestDto request) {
        Classroom checkClassroom = classroomRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        /*
         * Divided into 2 cases
         * 1. If there's an alternative teacher -> get new teacher Id
         * 2. Else no changes
         */
        Teacher teacher;
        if (request.getTeacherId() == null) {
            teacher = checkClassroom.getTeacher();
        } else {
            teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        if (request.isTeacherTaught()) {
            ensureNotLocked(teacher, request.getSessionDate());
        }

        ClassSession newClassSession = new ClassSession();
        newClassSession.setClassroom(checkClassroom);
        newClassSession.setTeacher(teacher);
        newClassSession.setSessionDate(request.getSessionDate());
        newClassSession.setTeacherTaught(request.isTeacherTaught());

        ClassSession saved = classSessionRepository.save(newClassSession);
        return toDto(saved);
    }

    public ClassSessionResponseDto updateTeacher(Long classSessionId, Long newTeacherId) {
        ClassSession checkClassSession = classSessionRepository.findById(classSessionId).orElseThrow(() -> new RuntimeException("Class session not found"));

        Teacher checkTeacher = teacherRepository.findById(newTeacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (checkClassSession.isTeacherTaught()) {
            ensureNotLocked(checkClassSession.getTeacher(), checkClassSession.getSessionDate());
            ensureNotLocked(checkTeacher, checkClassSession.getSessionDate());
        }

        checkClassSession.setTeacher(checkTeacher);
        ClassSession updated = classSessionRepository.save(checkClassSession);
        return toDto(updated);
    }

    public ClassSessionResponseDto updateIsTaught(Long sessionId, boolean isTaught) {
        ClassSession checkSession = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Class session not found"));

        ensureNotLocked(checkSession.getTeacher(), checkSession.getSessionDate());

        checkSession.setTeacherTaught(isTaught);
        ClassSession updated = classSessionRepository.save(checkSession);
        return toDto(updated);
    }

    private void ensureNotLocked(Teacher teacher, LocalDate sessionDate) {
        int month = sessionDate.getMonthValue();
        int year = sessionDate.getYear();

        teacherPayrollRepository.findByTeacherIdAndMonthAndYear(teacher.getId(), month, year)
                .filter(p -> p.getStatus() != PayrollStatus.DRAFT)
                .ifPresent(p -> {
                    throw new BusinessRuleException("Payroll of " + teacher.getUser().getFullName()
                            + " for " + month + "/" + year + " is " + p.getStatus()
                            + " and locked, so its sessions can no longer be changed.");
                });
    }

    private ClassSessionResponseDto toDto(ClassSession classSession) {
        return new ClassSessionResponseDto(classSession.getId(), classSession.getClassroom().getId(),
                classSession.getTeacher().getId(), classSession.getSessionDate(), classSession.isTeacherTaught());
    }
}
