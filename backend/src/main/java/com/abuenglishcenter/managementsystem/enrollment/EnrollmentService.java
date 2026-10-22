package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.Classroom;
import com.abuenglishcenter.managementsystem.classroom.ClassroomRepository;
import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;
import com.abuenglishcenter.managementsystem.student.Student;
import com.abuenglishcenter.managementsystem.student.StudentRepository;
import com.abuenglishcenter.managementsystem.student.StudentStatus;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
            ClassroomRepository classroomRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream().map(this::toDto).toList();
    }

    public EnrollmentResponseDto getEnrollmentById(Long id) {
        Enrollment checkEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment with id " + id + " not found"));
        return toDto(checkEnrollment);
    }

    @Transactional
    public EnrollmentResponseDto createEnrollment(EnrollmentCreateRequestDto request) {
        Student checkStudent = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student with id " + request.getStudentId() + " not found"));
        Classroom checkClassroom = classroomRepository.findById(request.getClassId())
                .orElseThrow(() -> new NotFoundException("Classroom with id " + request.getClassId() + " not found"));

        if (checkStudent.getStatus() != StudentStatus.ACTIVE) {
            throw new BusinessRuleException("Student with id " + checkStudent.getId() + " is "
                    + checkStudent.getStatus() + ". Only ACTIVE students can be enrolled.");
        }

        if (enrollmentRepository.existsByStudentIdAndClassroomIdAndStatus(checkStudent.getId(), checkClassroom.getId(),
                EnrollmentStatus.ACTIVE)) {
            throw new BusinessRuleException("Student with id " + checkStudent.getId()
                    + " already has an active enrollment in class " + checkClassroom.getId() + ".");
        }

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudent(checkStudent);
        newEnrollment.setClassroom(checkClassroom);
        newEnrollment.setEnrolledDate(request.getEnrolledDate());
        newEnrollment.setStatus(EnrollmentStatus.ACTIVE);

        try {
            return toDto(enrollmentRepository.saveAndFlush(newEnrollment));
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Student with id " + checkStudent.getId()
                    + " already has an active enrollment in class " + checkClassroom.getId() + ".");
        }
    }

    @Transactional
    public EnrollmentResponseDto updateEnrollment(Long id, LocalDate newEnrolledDate) {
        Enrollment checkEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment with id " + id + " not found"));

        checkEnrollment.setEnrolledDate(newEnrolledDate);

        return toDto(enrollmentRepository.save(checkEnrollment));
    }

    @Transactional
    public EnrollmentResponseDto updateEnrollmentStatus(Long id, EnrollmentStatus newStatus) {
        Enrollment checkEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment with id " + id + " not found"));

        if (checkEnrollment.getStatus() == newStatus) {
            throw new BusinessRuleException("Enrollment " + id + " is already " + newStatus + ".");
        }

        if (!checkEnrollment.getStatus().canMoveTo(newStatus)) {
            throw new BusinessRuleException(
                    "Cannot change enrollment status from " + checkEnrollment.getStatus() + " to " + newStatus + ".");
        }

        checkEnrollment.setStatus(newStatus);
        return toDto(enrollmentRepository.save(checkEnrollment));
    }

    private EnrollmentResponseDto toDto(Enrollment enrollment) {
        return new EnrollmentResponseDto(enrollment.getId(), enrollment.getStudent().getId(),
                enrollment.getClassroom().getId(), enrollment.getEnrolledDate(), enrollment.getStatus());
    }
}
