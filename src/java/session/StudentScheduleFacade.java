/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ProjectSchedule;
import entity.StudentSchedule;
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
public class StudentScheduleFacade extends AbstractFacade<StudentSchedule> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentScheduleFacade() {
        super(StudentSchedule.class);
    }

    public List<StudentSchedule> findByStudentID(String studentID) {
        Query q = em.createNamedQuery("StudentSchedule.findByStudentID");
        q.setParameter("studentID", studentID);
        return q.getResultList();
    }
}
