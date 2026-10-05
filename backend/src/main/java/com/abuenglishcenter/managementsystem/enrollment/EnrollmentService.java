package com.abuenglishcenter.managementsystem.enrollment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.classroom.Classroom;
import com.abuenglishcenter.managementsystem.classroom.ClassroomRepository;
import com.abuenglishcenter.managementsystem.student.Student;
import com.abuenglishcenter.managementsystem.student.StudentRepository;

@Service 
public class EnrollmentService {

    @Autowired 
    private EnrollmentRepository enrollmentRepository;

    @Autowired 
    private StudentRepository studentRepository;

    @Autowired 
    private ClassroomRepository classroomRepository;

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
