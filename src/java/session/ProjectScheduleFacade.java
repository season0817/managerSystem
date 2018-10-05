/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ProjectSchedule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dawson
 */
@Stateless
public class ProjectScheduleFacade extends AbstractFacade<ProjectSchedule> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectScheduleFacade() {
        super(ProjectSchedule.class);
    }

    public List<ProjectSchedule> findByProjectID(String projectID) {
        Query q = em.createNamedQuery("ProjectSchedule.findByProjectID");
        q.setParameter("projectID", projectID);
        return q.getResultList();
    }

}
