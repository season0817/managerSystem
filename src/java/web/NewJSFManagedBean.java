/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Project;
import entity.Student;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import session.ProjectFacade;
import session.StudentFacade;

/**
 *
 * @author Dawson
 */
@Named(value = "newJSFManagedBean")
@SessionScoped
public class NewJSFManagedBean implements Serializable {
    private String student_id;
    private String project_id;
    private  List<Student> student;
    private  List<Student> Group;
    private  List<Project> Project;
    @EJB
    private  StudentFacade studentFacade;
    @EJB
    private  ProjectFacade projectFacade;
    private  Project user_project;
    ManagedBean managedBean;
    /**
     * Creates a new instance of NewJSFManagedBean
     */
    public NewJSFManagedBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        managedBean = (ManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "managedBean");
        student_id=managedBean.getStudentId();
        project_id=managedBean.getProjectId();
    }

    public List<Student> getstudent() {
        student=studentFacade.findAll();
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }
    
    public List<Student> getGroup() {
        /*原来的todo根据studentid找projectid,再找出projectid对应的List<Student>,适用于原来的project,student一对多关系，现在不适用，要改造，该bean需要projectid*/
       // Group=studentFacade.todo(student_id);
       Group=studentFacade.findStudentsByPID(project_id);
        return Group;
    }
    
    public void setGroup(List<Student> Group) {
        this.Group = Group;
    }

    public List<Project> getProject() {
        return Project;
    }

    public void setProject(List<Project> Project) {
        this.Project = Project;
    }

    public Project getUser_project() {
        //projectFacade.find(project_id)
        this.user_project=projectFacade.find(project_id);
       // this.user_project=studentFacade.find(student_id).getProject();
        return user_project;
    }

    public void setUser_project(Project user_project) {
        this.user_project = user_project;
    }


    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    /**
     * @return the project_id
     */
    public String getProject_id() {
        return project_id;
    }

    /**
     * @param project_id the project_id to set
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
