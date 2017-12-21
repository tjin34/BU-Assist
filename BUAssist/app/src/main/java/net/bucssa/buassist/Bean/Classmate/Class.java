package net.bucssa.buassist.Bean.Classmate;

import java.io.Serializable;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class Class implements Serializable{

    /**
     * classId : 19
     * classCode : CS 101
     * className : Intro to Computer Science
     * classGroupId : 10
     * classGroup : Computer Science
     * professorId : 25
     * professorName : Wayne Snyder
     * ScheduleString : MTF 9:00~10:00
     * studentCount : 36
     * groupCount : 20
     */

    private int classId;
    private String classCode;
    private String className;
    private int classGroupId;
    private String classGroup;
    private int professorId;
    private String professorName;
    private String ScheduleString;
    private int studentCount;
    private int groupCount;

    public Class(String classCode, String className, String classGroup,
                 String professorName, int studentCount, int groupCount) {
        this.classCode = classCode;
        this.className = className;
        this.classGroup = classGroup;
        this.professorName = professorName;
        this.studentCount = studentCount;
        this.groupCount = groupCount;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassGroupId() {
        return classGroupId;
    }

    public void setClassGroupId(int classGroupId) {
        this.classGroupId = classGroupId;
    }

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getScheduleString() {
        return ScheduleString;
    }

    public void setScheduleString(String ScheduleString) {
        this.ScheduleString = ScheduleString;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }
}
