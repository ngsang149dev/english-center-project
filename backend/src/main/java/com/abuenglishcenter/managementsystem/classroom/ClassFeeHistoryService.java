package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassFeeHistoryService {

    @Autowired 
    private ClassroomRepository classroomRepository;

    @Autowired 
    private ClassFeeHistoryRepository classFeeHistoryRepository;

    public List<ClassFeeHistoryResponseDto> getAllHistoryFees() {
        return classFeeHistoryRepository.findAll().stream().map(this::toDto).toList();
    }

    private void closeCurrentActiveFee(Long classroomId, LocalDate newEffectiveDate) {
        //Find the activating record (isActive == true) (effectiveTo == null)
        List<ClassFeeHistory> activeFees = classFeeHistoryRepository.findByClassroomIdAndEffectiveToIsNull(classroomId);
        for (ClassFeeHistory oldFee : activeFees) {
            oldFee.setEffectiveTo(newEffectiveDate.minusDays(1));
            classFeeHistoryRepository.save(oldFee);
        }
    }

    public ClassFeeHistoryResponseDto createFeeHistory(ClassFeeHistoryCreateRequestDto request) {
        Classroom checkClassroom = classroomRepository.findById(request.getClassId())
        .orElseThrow(() -> new RuntimeException("Classroom not found"));

        closeCurrentActiveFee(request.getClassId(), request.getEffectiveFrom());
        //Create a new record
        ClassFeeHistory newFee = new ClassFeeHistory();
        newFee.setClassroom(checkClassroom);
        newFee.setMonthlyFee(request.getMonthlyFee());
        newFee.setEffectiveFrom(request.getEffectiveFrom());
        newFee.setEffectiveTo(null); // activating

        ClassFeeHistory saved = classFeeHistoryRepository.save(newFee);
        return toDto(saved);
    }

    private ClassFeeHistoryResponseDto toDto(ClassFeeHistory classFeeHistory) {
        return new ClassFeeHistoryResponseDto(classFeeHistory.getId(), classFeeHistory.getClassroom().getId(), classFeeHistory.getMonthlyFee(), classFeeHistory.getEffectiveFrom(), classFeeHistory.getEffectiveTo());
    }
}
