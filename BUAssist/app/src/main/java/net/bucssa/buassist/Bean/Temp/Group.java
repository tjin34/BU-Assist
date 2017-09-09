package net.bucssa.buassist.Bean.Temp;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class Group {

    private String name;
    private String classID;
    private String creator;
    private int num_studs;

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum_studs(int num_studs) {
        this.num_studs = num_studs;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public String getClassID() {
        return classID;
    }

    public int getNum_studs() {
        return num_studs;
    }

    public String getCreator() {
        return creator;
    }

}
