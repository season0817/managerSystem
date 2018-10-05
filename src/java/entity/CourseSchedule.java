/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author seasom
 */
@Entity
@Table(name = "course_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseSchedule.findAll", query = "SELECT c FROM CourseSchedule c")
    , @NamedQuery(name = "CourseSchedule.findByDate", query = "SELECT c FROM CourseSchedule c WHERE c.courseSchedulePK.date = :date")
    , @NamedQuery(name = "CourseSchedule.findByCourseID", query = "SELECT c FROM CourseSchedule c WHERE c.courseSchedulePK.courseID = :courseID")})
public class CourseSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CourseSchedulePK courseSchedulePK;
    @Lob
    @Size(max = 65535)
    @Column(name = "AMContent")
    private String aMContent;
    @Lob
    @Size(max = 65535)
    @Column(name = "PMContent")
    private String pMContent;
    @JoinColumn(name = "course_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;

    public CourseSchedule() {
    }

    public CourseSchedule(CourseSchedulePK courseSchedulePK) {
        this.courseSchedulePK = courseSchedulePK;
    }

    public CourseSchedule(Date date, String courseID) {
        this.courseSchedulePK = new CourseSchedulePK(date, courseID);
    }

    public CourseSchedulePK getCourseSchedulePK() {
        return courseSchedulePK;
    }

    public void setCourseSchedulePK(CourseSchedulePK courseSchedulePK) {
        this.courseSchedulePK = courseSchedulePK;
    }

    public String getAMContent() {
        return aMContent;
    }

    public void setAMContent(String aMContent) {
        this.aMContent = aMContent;
    }

    public String getPMContent() {
        return pMContent;
    }

    public void setPMContent(String pMContent) {
        this.pMContent = pMContent;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseSchedulePK != null ? courseSchedulePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseSchedule)) {
            return false;
        }
        CourseSchedule other = (CourseSchedule) object;
        if ((this.courseSchedulePK == null && other.courseSchedulePK != null) || (this.courseSchedulePK != null && !this.courseSchedulePK.equals(other.courseSchedulePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CourseSchedule[ courseSchedulePK=" + courseSchedulePK + " ]";
    }
    
}
