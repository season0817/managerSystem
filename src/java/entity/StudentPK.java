/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author seasom
 */
@Embeddable
public class StudentPK implements Serializable {

    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 45)
    @Column(name = "project_ID")
    private String projectID;
    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 45)
    @Column(name = "course_ID")
    private String courseID;

    public StudentPK() {
    }

    public StudentPK(String id, String projectID, String courseID) {
        this.id = id;
        this.projectID = projectID;
        this.courseID = courseID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        hash += (id != null ? id.hashCode() : 0);
        hash += (projectID != null ? projectID.hashCode() : 0);
        hash += (courseID != null ? courseID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentPK)) {
            return false;
        }
        StudentPK other = (StudentPK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return "entity.StudentPK[ id=" + id + ", projectID=" + projectID + ", courseID=" + courseID + " ]";
    }
    
}
