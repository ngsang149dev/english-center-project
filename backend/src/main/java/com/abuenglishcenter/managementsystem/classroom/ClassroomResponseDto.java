package com.abuenglishcenter.managementsystem.classroom;

public class ClassroomResponseDto {
    private Long id;
    private String name;
    private Long teacherId;
    private String curriculumSheetUrl;
    
    public ClassroomResponseDto(Long id, String name, Long teacherId, String curriculumSheetUrl) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.curriculumSheetUrl = curriculumSheetUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getCurriculumSheetUrl() {
        return curriculumSheetUrl;
    }

}
