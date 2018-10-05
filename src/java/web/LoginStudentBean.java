/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Course;
import entity.Student;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import session.ManagerFacade;
import session.StudentFacade;
import session.UserstudentFacade;

/**
 *
 * @author seasom
 */
@Named(value = "loginStudentBean")
@SessionScoped
public class LoginStudentBean implements Serializable {

    private String studentId;
    private String studentPassword;
    private String studentName;
    
    private String managerId;
    private String managerName;
    private String managerPassword;
    
    private List<Student> students;
    private List<Course> courses;
    @EJB
    private UserstudentFacade userstudentFacade;
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ManagerFacade managerFacade;
    /**
     * Creates a new instance of LoginStudentBean
     */
    public LoginStudentBean() {
    }

    /*user student 登录*/
    public String studentLogin() {
        System.out.println("studentlogin");
        System.out.println(studentId);
        if ("success".equals(getUserstudentFacade().isLogin(studentId, studentPassword))) {
            /*登录成功，初始化<List>student  展示在student_info(展示学生所有的project records)*/
            //userStudent = studentFacade.find(studentId);
             System.out.println("studentlogin success");
            return "student_info";
        } else {
            this.studentLogout();
            /*登录失败，保持在login_userStudent页面*/
             System.out.println(studentPassword);
            return null;
        }
    }
    
     public String managerLogin() {
        if ("success".equals(managerFacade.isLogin(getManagerId(), getManagerName(), getManagerPassword()))) {
            return "manager_welcome";
        } else {
            this.managerLogout();
            return null;
        }
    }
    
    public String studentName(String studentId){
        return userstudentFacade.find(studentId).getName();
    }
    
    
    /*student 登出*/
    public void studentLogout() {
        studentId = null;
        studentName = null;
    }
    
    public void managerLogout() {
        managerId = null;
        managerName = null;
        managerPassword = null;
    }
    
     public String logout() {
        studentId = null;
        studentName = null;
        return "index";
    }
    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the studentPassword
     */
    public String getStudentPassword() {
        return studentPassword;
    }

    /**
     * @param studentPassword the studentPassword to set
     */
    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return the userstudentFacade
     */
    public UserstudentFacade getUserstudentFacade() {
        return userstudentFacade;
    }

    /**
     * @param userstudentFacade the userstudentFacade to set
     */
    public void setUserstudentFacade(UserstudentFacade userstudentFacade) {
        this.userstudentFacade = userstudentFacade;
    }

    /**
     * @return the students
     */
    public List<Student> getStudents() {
        students=studentFacade.findStudents(studentId);
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * @return the managerId
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * @param managerId the managerId to set
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    /**
     * @return the managerName
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * @param managerName the managerName to set
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * @return the managerPassword
     */
    public String getManagerPassword() {
        return managerPassword;
    }

    /**
     * @param managerPassword the managerPassword to set
     */
    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    /**
     * @return the courses
     */
    public List<Course> getCourses() {
        courses=(List<Course>) managerFacade.find(managerId).getCourseCollection();
        return courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    
}
