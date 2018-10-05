/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author seasom
 */
@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    , @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.studentPK.id = :id")
    , @NamedQuery(name = "Student.findByName", query = "SELECT s FROM Student s WHERE s.name = :name")
    , @NamedQuery(name = "Student.findByTotalMark", query = "SELECT s FROM Student s WHERE s.totalMark = :totalMark")
    , @NamedQuery(name = "Student.findByProjectID", query = "SELECT s FROM Student s WHERE s.studentPK.projectID = :projectID")
    , @NamedQuery(name = "Student.findByIdPId", query = "SELECT s FROM Student s WHERE  s.studentPK.id = :id and s.studentPK.projectID = :projectID")
        , @NamedQuery(name = "Student.findByIdCId", query = "SELECT s FROM Student s WHERE  s.studentPK.id = :id and s.studentPK.courseID = :courseID")
    , @NamedQuery(name = "Student.findByCourseID", query = "SELECT s FROM Student s WHERE s.studentPK.courseID = :courseID")})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StudentPK studentPK;
    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Lob
    @Size(max = 16777215)
    @Column(name = "Report")
    private String report;
    @Size(max = 45)
    @Column(name = "TotalMark")
    private String totalMark;
    @ManyToMany(mappedBy = "studentCollection")
    private Collection<Manager> managerCollection;
    @JoinColumn(name = "course_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;
    @JoinColumn(name = "project_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Project project;
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Userstudent userstudent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<StudentSchedule> studentScheduleCollection;

    public Student() {
    }

    public Student(StudentPK studentPK) {
        this.studentPK = studentPK;
    }

    public Student(StudentPK studentPK, String name) {
        this.studentPK = studentPK;
        this.name = name;
    }

    public Student(String id, String projectID, String courseID) {
        this.studentPK = new StudentPK(id, projectID, courseID);
    }

    public StudentPK getStudentPK() {
        return studentPK;
    }

    public void setStudentPK(StudentPK studentPK) {
        this.studentPK = studentPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(String totalMark) {
        this.totalMark = totalMark;
    }

    @XmlTransient
    public Collection<Manager> getManagerCollection() {
        return managerCollection;
    }

    public void setManagerCollection(Collection<Manager> managerCollection) {
        this.managerCollection = managerCollection;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Userstudent getUserstudent() {
        return userstudent;
    }

    public void setUserstudent(Userstudent userstudent) {
        this.userstudent = userstudent;
    }

    @XmlTransient
    public Collection<StudentSchedule> getStudentScheduleCollection() {
        return studentScheduleCollection;
    }

    public void setStudentScheduleCollection(Collection<StudentSchedule> studentScheduleCollection) {
        this.studentScheduleCollection = studentScheduleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentPK != null ? studentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.studentPK == null && other.studentPK != null) || (this.studentPK != null && !this.studentPK.equals(other.studentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Student[ studentPK=" + studentPK + " ]";
    }
    
}
