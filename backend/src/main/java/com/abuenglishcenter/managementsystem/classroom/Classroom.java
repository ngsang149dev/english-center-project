package com.abuenglishcenter.managementsystem.classroom;

import com.abuenglishcenter.managementsystem.teacher.Teacher;
import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private String curriculumSheetUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCurriculumSheetUrl() {
        return curriculumSheetUrl;
    }

    public void setCurriculumSheetUrl(String curriculumSheetUrl) {
        this.curriculumSheetUrl = curriculumSheetUrl;
    }

}
