/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author seasom
 */
@Entity
@Table(name = "class_plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClassPlan.findAll", query = "SELECT c FROM ClassPlan c")
    , @NamedQuery(name = "ClassPlan.findById", query = "SELECT c FROM ClassPlan c WHERE c.id = :id")
    , @NamedQuery(name = "ClassPlan.findByStartDate", query = "SELECT c FROM ClassPlan c WHERE c.startDate = :startDate")
    , @NamedQuery(name = "ClassPlan.findByEndDate", query = "SELECT c FROM ClassPlan c WHERE c.endDate = :endDate")
    , @NamedQuery(name = "ClassPlan.findByProjectLimitNumber", query = "SELECT c FROM ClassPlan c WHERE c.projectLimitNumber = :projectLimitNumber")})
public class ClassPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "ProjectLimitNumber")
    private Integer projectLimitNumber;
    @OneToMany(mappedBy = "classplanID")
    private Collection<Course> courseCollection;

    public ClassPlan() {
    }

    public ClassPlan(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getProjectLimitNumber() {
        return projectLimitNumber;
    }

    public void setProjectLimitNumber(Integer projectLimitNumber) {
        this.projectLimitNumber = projectLimitNumber;
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
        if (!(object instanceof ClassPlan)) {
            return false;
        }
        ClassPlan other = (ClassPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ClassPlan[ id=" + id + " ]";
    }
    
}
