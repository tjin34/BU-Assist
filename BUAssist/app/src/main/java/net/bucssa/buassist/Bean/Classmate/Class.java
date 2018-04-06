package net.bucssa.buassist.Bean.Classmate;

import java.io.Serializable;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class Class implements Serializable{


    /**
     * classId : 5
     * classCode : CS 666
     * classSection : A1
     * className : Computer Art
     * classSchedule : Tue, Thu 1:15-5:00 pm
     * classLocation : CAS 100
     * professorId : 6
     * professorName : Li Liu
     * isCollected:false
     */

    private int classId;
    private String classCode;
    private String classSection;
    private String className;
    private String classSchedule;
    private String classLocation;
    private int professorId;
    private String professorName;
    private boolean isCollected;

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

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(String classSchedule) {
        this.classSchedule = classSchedule;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
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

    public void setCollected(boolean collection) {
        isCollected = collection;
    }

    public boolean isCollected() {
        return isCollected;
    }
}
