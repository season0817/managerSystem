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
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
    , @NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name")
    , @NamedQuery(name = "Project.findByTotalMark", query = "SELECT p FROM Project p WHERE p.totalMark = :totalMark")
    , @NamedQuery(name = "Project.findByHeadman", query = "SELECT p FROM Project p WHERE p.headman = :headman")})
public class Project implements Serializable {

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
    @Column(name = "TotalMark")
    private String totalMark;
    @Lob
    @Size(max = 16777215)
    @Column(name = "Report")
    private String report;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Headman")
    private String headman;
    @ManyToMany(mappedBy = "projectCollection")
    private Collection<Manager> managerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Collection<Student> studentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Collection<ProjectSchedule> projectScheduleCollection;
    @JoinColumn(name = "course_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Course courseID;

    public Project() {
    }

    public Project(String id) {
        this.id = id;
    }

    public Project(String id, String headman) {
        this.id = id;
        this.headman = headman;
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

    public String getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(String totalMark) {
        this.totalMark = totalMark;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getHeadman() {
        return headman;
    }

    public void setHeadman(String headman) {
        this.headman = headman;
    }

    @XmlTransient
    public Collection<Manager> getManagerCollection() {
        return managerCollection;
    }

    public void setManagerCollection(Collection<Manager> managerCollection) {
        this.managerCollection = managerCollection;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @XmlTransient
    public Collection<ProjectSchedule> getProjectScheduleCollection() {
        return projectScheduleCollection;
    }

    public void setProjectScheduleCollection(Collection<ProjectSchedule> projectScheduleCollection) {
        this.projectScheduleCollection = projectScheduleCollection;
    }

    public Course getCourseID() {
        return courseID;
    }

    public void setCourseID(Course courseID) {
        this.courseID = courseID;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Project[ id=" + id + " ]";
    }
    
}
