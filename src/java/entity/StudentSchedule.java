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
import javax.persistence.JoinColumns;
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
@Table(name = "student_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentSchedule.findAll", query = "SELECT s FROM StudentSchedule s")
    , @NamedQuery(name = "StudentSchedule.findByDate", query = "SELECT s FROM StudentSchedule s WHERE s.studentSchedulePK.date = :date")
    , @NamedQuery(name = "StudentSchedule.findByStudentID", query = "SELECT s FROM StudentSchedule s WHERE s.studentSchedulePK.studentID = :studentID")
    , @NamedQuery(name = "StudentSchedule.findByProjectID", query = "SELECT s FROM StudentSchedule s WHERE s.studentSchedulePK.projectID = :projectID")
    , @NamedQuery(name = "StudentSchedule.findByCourseID", query = "SELECT s FROM StudentSchedule s WHERE s.studentSchedulePK.courseID = :courseID")})
public class StudentSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StudentSchedulePK studentSchedulePK;
    @Lob
    @Size(max = 65535)
    @Column(name = "AMContent")
    private String aMContent;
    @Lob
    @Size(max = 65535)
    @Column(name = "PMContent")
    private String pMContent;
    @JoinColumns({
        @JoinColumn(name = "student_ID", referencedColumnName = "ID", insertable = false, updatable = false)
        , @JoinColumn(name = "project_ID", referencedColumnName = "project_ID", insertable = false, updatable = false)
        , @JoinColumn(name = "course_ID", referencedColumnName = "course_ID", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Student student;

    public StudentSchedule() {
    }

    public StudentSchedule(StudentSchedulePK studentSchedulePK) {
        this.studentSchedulePK = studentSchedulePK;
    }

    public StudentSchedule(Date date, String studentID, String projectID, String courseID) {
        this.studentSchedulePK = new StudentSchedulePK(date, studentID, projectID, courseID);
    }

    public StudentSchedulePK getStudentSchedulePK() {
        return studentSchedulePK;
    }

    public void setStudentSchedulePK(StudentSchedulePK studentSchedulePK) {
        this.studentSchedulePK = studentSchedulePK;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentSchedulePK != null ? studentSchedulePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentSchedule)) {
            return false;
        }
        StudentSchedule other = (StudentSchedule) object;
        if ((this.studentSchedulePK == null && other.studentSchedulePK != null) || (this.studentSchedulePK != null && !this.studentSchedulePK.equals(other.studentSchedulePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StudentSchedule[ studentSchedulePK=" + studentSchedulePK + " ]";
    }
    
}
