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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
    , @NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id")
    , @NamedQuery(name = "Course.findByName", query = "SELECT c FROM Course c WHERE c.name = :name")
    , @NamedQuery(name = "Course.findByYear", query = "SELECT c FROM Course c WHERE c.year = :year")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "Name")
    private String name;
    @Size(max = 45)
    @Column(name = "year")
    private String year;
    @JoinTable(name = "course_has_userstudent", joinColumns = {
        @JoinColumn(name = "course_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "userStudent_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Userstudent> userstudentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<Student> studentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<CourseSchedule> courseScheduleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseID")
    private Collection<Project> projectCollection;
    @JoinColumn(name = "class_plan_ID", referencedColumnName = "ID")
    @ManyToOne
    private ClassPlan classplanID;
    @JoinColumn(name = "manager_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Manager managerID;

    public Course() {
    }

    public Course(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @XmlTransient
    public Collection<Userstudent> getUserstudentCollection() {
        return userstudentCollection;
    }

    public void setUserstudentCollection(Collection<Userstudent> userstudentCollection) {
        this.userstudentCollection = userstudentCollection;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @XmlTransient
    public Collection<CourseSchedule> getCourseScheduleCollection() {
        return courseScheduleCollection;
    }

    public void setCourseScheduleCollection(Collection<CourseSchedule> courseScheduleCollection) {
        this.courseScheduleCollection = courseScheduleCollection;
    }

    @XmlTransient
    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    public ClassPlan getClassplanID() {
        return classplanID;
    }

    public void setClassplanID(ClassPlan classplanID) {
        this.classplanID = classplanID;
    }

    public Manager getManagerID() {
        return managerID;
    }

    public void setManagerID(Manager managerID) {
        this.managerID = managerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Course[ id=" + id + " ]";
    }
    
}
