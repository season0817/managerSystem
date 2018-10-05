/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CourseSchedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
/**
 *
 * @author seasom
 */
@Stateless
public class CourseScheduleFacade extends AbstractFacade<CourseSchedule> {

   @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseScheduleFacade() {
        super(CourseSchedule.class);
    }
    
    public List<CourseSchedule> findByCID(String courseId){
        Query q=em.createNamedQuery("CourseSchedule.findByCourseID");
          q.setParameter("courseID", courseId);
           return q.getResultList();
    }
}
