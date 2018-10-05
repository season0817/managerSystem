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
@Table(name = "project_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectSchedule.findAll", query = "SELECT p FROM ProjectSchedule p")
    , @NamedQuery(name = "ProjectSchedule.findByDate", query = "SELECT p FROM ProjectSchedule p WHERE p.projectSchedulePK.date = :date")
    , @NamedQuery(name = "ProjectSchedule.findByProjectID", query = "SELECT p FROM ProjectSchedule p WHERE p.projectSchedulePK.projectID = :projectID")})
public class ProjectSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProjectSchedulePK projectSchedulePK;
    @Lob
    @Size(max = 65535)
    @Column(name = "AMContent")
    private String aMContent;
    @Lob
    @Size(max = 65535)
    @Column(name = "PMContent")
    private String pMContent;
    @JoinColumn(name = "project_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Project project;

    public ProjectSchedule() {
    }

    public ProjectSchedule(ProjectSchedulePK projectSchedulePK) {
        this.projectSchedulePK = projectSchedulePK;
    }

    public ProjectSchedule(Date date, String projectID) {
        this.projectSchedulePK = new ProjectSchedulePK(date, projectID);
    }

    public ProjectSchedulePK getProjectSchedulePK() {
        return projectSchedulePK;
    }

    public void setProjectSchedulePK(ProjectSchedulePK projectSchedulePK) {
        this.projectSchedulePK = projectSchedulePK;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectSchedulePK != null ? projectSchedulePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSchedule)) {
            return false;
        }
        ProjectSchedule other = (ProjectSchedule) object;
        if ((this.projectSchedulePK == null && other.projectSchedulePK != null) || (this.projectSchedulePK != null && !this.projectSchedulePK.equals(other.projectSchedulePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProjectSchedule[ projectSchedulePK=" + projectSchedulePK + " ]";
    }
    
}
