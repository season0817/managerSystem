/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Manager;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dawson
 */
@Stateless
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManagerFacade() {
        super(Manager.class);
    }

    public String isLogin(String managerId, String managerName, String managerPassword) {
        String flag = "falure";
        Query q = em.createQuery("SELECT m FROM Manager m WHERE m.id= :mid AND m.name= :mname AND m.password= :pass");
        q.setParameter("mid", managerId);
        q.setParameter("mname", managerName);
        q.setParameter("pass", managerPassword);
        try {
            Manager m = (Manager) q.getSingleResult();
            if (managerId.equals(m.getId())
                    && managerName.equals(m.getName())
                    && managerPassword.equals(m.getPassword())) {
                flag = "success";
            }
        } catch (Exception e) {
            return null;
        }
        return flag;
    }
}
