/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Course;
import entity.Project;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dawson
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }
    
    
    
            public void projectReportUploaded(String projectId) {
        Project p = this.find(projectId);
        if (p != null) {
            em.flush();
            p.setReport("1");
            em.persist(p);
        }
    }

//    @张文珍
    public void addProject(String id, String name, String totalmark, String report, String headman,Course course) {
        try {
            Project p = new Project();
            em.flush();
            p.setId(id);
            p.setName(name);
            p.setHeadman(headman);
            p.setReport(null);
            p.setTotalMark(null);
            p.setCourseID(course);
            em.persist(p);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public int countProject() {
        int count = 0;
        try {
            count = em.createQuery("findAll").getResultList().size();
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
        return count;
    }

    public void removeProject(String id) {
        try {
            Project p = this.find(id);
            em.remove(p);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void editProject(Project p) {
        Project p1 = new Project();
        em.flush();
        p1.setId(p.getId());
        p1.setName(p.getName());
        p1.setTotalMark(p.getTotalMark());
        p1.setHeadman(p.getHeadman());
        em.merge(p1);
    }

//    public void dropProject(Project p) {
//        try {
//            em.flush();
//            Project project = this.find(p.getId());
//            em.remove(project);
//            List<Student> s=studentFacade.findAll();
//            for(Student student:s){
//                em.refresh(student);
//            }
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//
//    }

    public void addProject(Project p) {
        Project p1 = new Project();
        em.flush();
        p1.setId(p.getId());
        p1.setName(p.getName());
        p1.setHeadman(p.getHeadman());
        em.persist(p1);
    }

    public void addProject(String id, String name, String headman) {
        try {
            Project p = new Project();
            em.flush();
            p.setId(id);
            p.setName(name);
            p.setHeadman(headman);
            em.persist(p);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
}
