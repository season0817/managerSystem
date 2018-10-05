/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author seasom
 */
@Embeddable
public class StudentSchedulePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "student_ID")
    private String studentID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "project_ID")
    private String projectID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "course_ID")
    private String courseID;

    public StudentSchedulePK() {
    }

    public StudentSchedulePK(Date date, String studentID, String projectID, String courseID) {
        this.date = date;
        this.studentID = studentID;
        this.projectID = projectID;
        this.courseID = courseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (date != null ? date.hashCode() : 0);
        hash += (studentID != null ? studentID.hashCode() : 0);
        hash += (projectID != null ? projectID.hashCode() : 0);
        hash += (courseID != null ? courseID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentSchedulePK)) {
            return false;
        }
        StudentSchedulePK other = (StudentSchedulePK) object;
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        if ((this.studentID == null && other.studentID != null) || (this.studentID != null && !this.studentID.equals(other.studentID))) {
            return false;
        }
        if ((this.projectID == null && other.projectID != null) || (this.projectID != null && !this.projectID.equals(other.projectID))) {
            return false;
        }
        if ((this.courseID == null && other.courseID != null) || (this.courseID != null && !this.courseID.equals(other.courseID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StudentSchedulePK[ date=" + date + ", studentID=" + studentID + ", projectID=" + projectID + ", courseID=" + courseID + " ]";
    }
    
}
