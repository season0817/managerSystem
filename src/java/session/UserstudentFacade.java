/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Userstudent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author seasom
 */
@Stateless
public class UserstudentFacade extends AbstractFacade<Userstudent> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserstudentFacade() {
        super(Userstudent.class);
    }
    
    public String isLogin(String studentId, String studentPassword) {
        String flag = "falure";
        System.out.println("call islogin");
        Query q = em.createQuery("SELECT u FROM Userstudent u WHERE u.id = :id AND u.password = :password");
        q.setParameter("id", studentId);
        q.setParameter("password", studentPassword);
        try {
            Userstudent u = (Userstudent) q.getSingleResult();
            if (studentId.equals(u.getId())
                    && studentPassword.equals(u.getPassword())) {
                return "success";
            } else {
                return "failure";
            }
        } catch (Exception e) {
            return null;
        }
    }
}
