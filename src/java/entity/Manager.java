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
@Table(name = "manager")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Manager.findAll", query = "SELECT m FROM Manager m")
    , @NamedQuery(name = "Manager.findById", query = "SELECT m FROM Manager m WHERE m.id = :id")
    , @NamedQuery(name = "Manager.findByName", query = "SELECT m FROM Manager m WHERE m.name = :name")
    , @NamedQuery(name = "Manager.findByPassword", query = "SELECT m FROM Manager m WHERE m.password = :password")})
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Password")
    private String password;
    @JoinTable(name = "manager_has_student", joinColumns = {
        @JoinColumn(name = "manager_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "student_ID", referencedColumnName = "ID")
        , @JoinColumn(name = "project_ID", referencedColumnName = "project_ID")
        , @JoinColumn(name = "course_ID", referencedColumnName = "course_ID")})
    @ManyToMany
    private Collection<Student> studentCollection;
    @JoinTable(name = "manager_has_project", joinColumns = {
        @JoinColumn(name = "manager_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "project_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Project> projectCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "managerID")
    private Collection<Course> courseCollection;

    public Manager() {
    }

    public Manager(String id) {
        this.id = id;
    }

    public Manager(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @XmlTransient
    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    @XmlTransient
    public Collection<Course> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<Course> courseCollection) {
        this.courseCollection = courseCollection;
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
        if (!(object instanceof Manager)) {
            return false;
        }
        Manager other = (Manager) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Manager[ id=" + id + " ]";
    }
    
}
