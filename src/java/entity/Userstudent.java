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
@Table(name = "userstudent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userstudent.findAll", query = "SELECT u FROM Userstudent u")
    , @NamedQuery(name = "Userstudent.findById", query = "SELECT u FROM Userstudent u WHERE u.id = :id")
    , @NamedQuery(name = "Userstudent.findByPassword", query = "SELECT u FROM Userstudent u WHERE u.password = :password")
    , @NamedQuery(name = "Userstudent.findByName", query = "SELECT u FROM Userstudent u WHERE u.name = :name")})
public class Userstudent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "Password")
    private String password;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "userstudentCollection")
    private Collection<Course> courseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userstudent")
    private Collection<Student> studentCollection;

    public Userstudent() {
    }

    public Userstudent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Course> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<Course> courseCollection) {
        this.courseCollection = courseCollection;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
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
        if (!(object instanceof Userstudent)) {
            return false;
        }
        Userstudent other = (Userstudent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Userstudent[ id=" + id + " ]";
    }
    
}
