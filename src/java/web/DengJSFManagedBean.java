/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Manager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import session.ManagerFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Dawson
 */
@ManagedBean(name = "dengJSFManagedBean")
@ViewScoped
public class DengJSFManagedBean implements Serializable {

    /**
     * Creates a new instance of DengJSFManagedBean
     */
    public DengJSFManagedBean() {

    }

    @EJB
    private session.ManagerFacade managerFacade;
    private List<Manager> managers = new ArrayList<>();
    //private Manager selected;
    private String manager_id;
    private String manager_name;
    private String manager_password;
    private Manager current;
    private String selectedManager;
    private List<Manager> filterManagers;

    @PostConstruct
    public void init() {
        managers = managerFacade.findAll();
    }
    
    public void updateDate(){
        managers = managerFacade.findAll();
        filterManagers=managers;
    }

    public List<Manager> getFilterManagers() {
        return filterManagers;
    }

    public void setFilterManagers(List<Manager> filterManagers) {
        this.filterManagers = filterManagers;
    }

    public ManagerFacade getManagerFacade() {
        return managerFacade;
    }

    public List<Manager> getManagers() {
//        if (managers.isEmpty()) {
//            managers = getManagerFacade().findAll();
//        }
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getSelectedManager() {
        return selectedManager;
    }

    public void setSelectedManager(String selectedManager) {
        this.selectedManager = selectedManager;
    }

    public void addManager() {
        if (manager_id != null && !manager_id.isEmpty()
                && manager_name != null && !manager_name.isEmpty()
                && manager_id != null && !manager_password.isEmpty()) {
            Manager m = managerFacade.find(manager_id);
            if (m == null && manager_name != null && manager_password != null) {
                current = new Manager();
                current.setId(manager_id);
                current.setName(manager_name);
                current.setPassword(manager_password);
                managerFacade.create(current);
                managers = getManagerFacade().findAll();
            }
        }else{
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "添加失败\n三个值都不能为空", ""));
        }
    }

    public void removeManager() {
        current = managerFacade.find(selectedManager);
        if ("1".equals(current.getId())) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "删除失败\n该用户为终极管理员，不允许被删除", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            managerFacade.remove(current);
            managers = managerFacade.findAll();
        }
    }

    public String getManager_password() {
        return manager_password;
    }

    public void setManager_password(String manager_password) {
        this.manager_password = manager_password;
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("修改成功\n"+"ManagerID："+((Manager) event.getObject()).getId(),"");
        current = (Manager) event.getObject();
        managerFacade.edit(current);
        managers = managerFacade.findAll();
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
    }
}
