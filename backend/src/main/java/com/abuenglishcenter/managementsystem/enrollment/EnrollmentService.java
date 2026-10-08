package com.abuenglishcenter.managementsystem.enrollment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.classroom.Classroom;
import com.abuenglishcenter.managementsystem.classroom.ClassroomRepository;
import com.abuenglishcenter.managementsystem.student.Student;
import com.abuenglishcenter.managementsystem.student.StudentRepository;

@Service 
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, ClassroomRepository classroomRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream().map(this::toDto).toList();
    }

    public EnrollmentResponseDto createEnrollment(EnrollmentCreateRequestDto request) {
        Student checkStudent = studentRepository.findById(request.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found"));
        Classroom checkClassroom = classroomRepository.findById(request.getClassId()).orElseThrow(() -> new RuntimeException("Class not found"));

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudent(checkStudent);
        newEnrollment.setClassroom(checkClassroom);
        newEnrollment.setEnrolledDate(request.getEnrolledDate());
        newEnrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment saved = enrollmentRepository.save(newEnrollment);
        return toDto(saved);
    }

    private EnrollmentResponseDto toDto(Enrollment enrollment) {
        return new EnrollmentResponseDto(enrollment.getId(), enrollment.getStudent().getId(), enrollment.getClassroom().getId(), enrollment.getEnrolledDate(), enrollment.getStatus());
    }
}
