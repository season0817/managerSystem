/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Course;
import entity.CourseSchedule;
import entity.Manager;
import entity.Project;
import entity.Student;
import java.io.Serializable;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import session.CourseFacade;
import session.CourseScheduleFacade;
import session.ManagerFacade;
import session.ProjectFacade;
import session.StudentFacade;

/**
 *
 * @author Dawson
 */
@Named(value = "managedBean")
@SessionScoped
public class ManagedBean implements Serializable {

    @EJB
    private ManagerFacade managerFacade;
    private List<Manager> managers;

    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private CourseFacade courseFacade;
    @EJB
    private CourseScheduleFacade courseScheduleFacade;

    private List<Student> students;
    private List<Student> projects;
    private List<CourseSchedule> courseSchedules;

    private Student student;
    private Project project;
    
    private Course course;

    private String studentId;
    private String studentName;
    private String managerId;
    private String managerName;
    private String managerPassword;
    private String editProjectName;

    private String projectId;
    private String courseId;
            
    LoginStudentBean loginStudentBean;
    public ManagedBean() {
         FacesContext context = FacesContext.getCurrentInstance();
        loginStudentBean = (LoginStudentBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "loginStudentBean");
        studentId=loginStudentBean.getStudentId();
        System.out.println("ManagedBean()");
        System.out.println(studentId);
    }

    public String studentLogin() {
        if ("success".equals(studentFacade.isLogin(studentId, studentName))) {
            student = studentFacade.find(studentId);
            return "student_welcome";
        } else {
            this.studentLogout();
            return null;
        }

    }

     public String checkMore() {
        if ("success".equals(studentFacade.isExist(studentId, projectId,courseId))) {
            //student = studentFacade.find(studentId);
            return "student_project";
        } else {
            this.studentLogout();
            return null;
        }

    }
    
    public String managerLogin() {
        if ("success".equals(managerFacade.isLogin(managerId, managerName, managerPassword))) {
            return "manager_welcome";
        } else {
            this.managerLogout();
            return null;
        }
    }

    public boolean isLoggedIn() {
        boolean flag = false;
        System.out.println("studentId:" + studentId + ",     managerId:" + managerId);
        if ("success".equals(studentFacade.isLogin(studentId, studentName)) || "success".equals(managerFacade.isLogin(managerId, managerName, managerPassword))) {
            flag = true;
        }
        return flag;
    }

    public String logout() {
        studentId = null;
        studentName = null;
        managerId = null;
        managerName = null;
        managerPassword = null;
        return "index";
    }

    public Project findProject(String projectId) {
        Project p = projectFacade.find(projectId);
        return p;
    }

    public void managerLogout() {
        managerId = null;
        managerName = null;
        managerPassword = null;
    }

    public void studentLogout() {
        studentId = null;
        studentName = null;
    }

    public void changeProjectName() {
        Student s = student;
        /*
        if (s.getProject() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("您还没有参与项目", ""));
            //之前是比较headman与id
        } else if (s.getProject().getHeadman().compareTo(s.getName()) != 0) {
        */
        if(s.getProject().getHeadman().compareTo(s.getName()) != 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("您不是项目负责人，没有权限修改项目名称", ""));
        } else if (project.getName() == null || project.getName().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("项目名称不能为空", ""));
        } else {
            this.projectFacade.edit(project);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("修改成功", ""));
        }
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }

    public List<Student> getStudents() {
        return studentFacade.findAll();
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getStudentName() {
        System.out.println("managedBean.getStudentName()");
        studentName=studentFacade.findStudent(studentId,projectId).getName();
        System.out.println("managedBean.getStudentName()ok");
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Student getStudent() {
        System.out.println("managedBean.getStudent()");
        Student s = studentFacade.findStudent(studentId,projectId);
         System.out.println("managedBean.getStudent()ok");
          System.out.println(s);
        return s;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    //新表中，find(studentId)返回List<student>,放入LoginStudentBean
    //这里  projectFacade.find(projectId)
    public Project getProject() {
        //project=studentFacade.find(studentId).getProject(); 
        project=projectFacade.find(projectId);
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the courseId
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
        course=courseFacade.find(courseId);
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return the courseSchedules
     */
    public List<CourseSchedule> getCourseSchedules() {
        courseSchedules=(List<CourseSchedule>) courseScheduleFacade.find(courseId);
        return courseSchedules;
    }

    /**
     * @param courseSchedules the courseSchedules to set
     */
    public void setCourseSchedules(List<CourseSchedule> courseSchedules) {
        this.courseSchedules = courseSchedules;
    }

}
