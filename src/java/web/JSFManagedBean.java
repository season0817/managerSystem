/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import javax.faces.bean.ManagedBean;
import entity.ClassPlan;
import entity.Course;
import entity.Project;
import entity.Student;
import entity.StudentPK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import session.ClassPlanFacade;
import session.CourseFacade;
import session.ProjectFacade;
import session.StudentFacade;
import session.UserstudentFacade;

/**
 *
 * @author 张文珍
 */
@ManagedBean(name = "jSFManagedBean")
@ViewScoped
public class JSFManagedBean implements Serializable {

    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ClassPlanFacade classPlanFacade;
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private CourseFacade courseFacade;
    @EJB
    private UserstudentFacade userstudentFacade;

    private List<Project> project;
    private List<Student> student = new ArrayList<>();
    private String currentStudent;
    private String currentProject;
    private int countProject;
    private String rowProject;
    web.ManagedBean managedBean;
    web.LoginStudentBean loginStudentBean;
    private String name;
    private String totalmark;
    private String report;

    private String newProjectId;
    private String newCourseId;

    private String oldProjectId;
    private String oldCourseId;
    
    private Project selectedProject;
    private List<Project> filteredProjects;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        loginStudentBean = (web.LoginStudentBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "loginStudentBean");
        currentStudent = loginStudentBean.getStudentId();
        //创建的项目id不能是学号，projectId要唯一
//        创建的项目ID是他自己的学号
        currentProject = currentStudent;
    }

    public void joinProject() {
        System.out.println("joinProject." + this.currentStudent);
        Project p = projectFacade.find(this.rowProject);
        List<Student> studentList = studentFacade.findAll();
        int count = 0;
        //计算该组已有人数
        for (Student student : studentList) {
          //  if (p.equals(student.getProject().getId())) {
          if(this.rowProject.equals(student.getStudentPK().getProjectID())){
                count++;
            }
        }
        String courseId = p.getCourseID().getId();
        String classplanId = courseFacade.find(courseId).getClassplanID().getId();
        ClassPlan classPlan = this.classPlanFacade.find(classplanId);
        StudentPK studentPK = new StudentPK(this.currentStudent, this.rowProject, courseId);
        Student s = studentFacade.find(studentPK);
        Student student = new Student(studentPK);
        System.out.println("joinProject." + s);
        if (s == null) {
            if (count < classPlan.getProjectLimitNumber()) {
                studentFacade.addProjectID(p.getId(), this.currentStudent, courseId);
                //studentFacade.addProjectID(student);
                FacesMessage message = new FacesMessage("参加项目成功", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage("该项目已经满人", "如果想突破这个限制可以联系老师");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage("您已参加该项目小组，请先退出", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void exitProject() {
        Student s = studentFacade.find(this.currentStudent);
        Project ps = s.getProject();
        Project p = projectFacade.find(this.rowProject);
        if (p.equals(ps)) {
            studentFacade.delProjectID(s);
            if (projectFacade.find(rowProject).getHeadman().equals(this.currentStudent)) {
                projectFacade.removeProject(this.rowProject);
                FacesMessage message = new FacesMessage("该项目已删除", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            FacesMessage message = new FacesMessage("退出项目成功", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("您没有参与该项目", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void removeProject() {
       StudentPK studentPK = new StudentPK(this.currentProject, oldProjectId, oldCourseId);
        Student s = studentFacade.find(studentPK);
       // Project ps = s.getProject();
       Project ps=projectFacade.find(oldProjectId);
        System.out.println("student"+s);
        System.out.println("project"+ps);
        if (ps != null&&s!=null) {
            studentFacade.delProjectID(s);
//            如果他是组长则删除项目
            if (ps.getHeadman().equals(this.currentProject)) {
                projectFacade.removeProject(ps.getId());
                FacesMessage message = new FacesMessage("您的项目已删除", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            FacesMessage message = new FacesMessage("退出项目成功", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("您尚未有参与项目", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void submitProject() {
        // Student s = studentFacade.find(this.currentStudent);
        //headman为负责人ID

        Student s = studentFacade.findStudentsByIDCID(this.currentStudent, newCourseId);
      //  String headman = userstudentFacade.find(this.currentStudent).getName();
      String headman=this.currentStudent;
        System.out.println("headman:" + headman);
        //查找projectId是否已经存在，projectId需要是唯一的
        Project p = projectFacade.find(newProjectId);
        //查找是否是正确存在的courseId
        Course c = courseFacade.find(newCourseId);
        if (name == null || name.isEmpty()) {
            FacesMessage message = new FacesMessage("项目名称不能为空", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (s != null) {
            FacesMessage message = new FacesMessage("你已经有该课程的项目小组，退出原小组前无法创建该小组");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            if (p == null && c != null) {
                //如果课程ID存在且projectId不存在，就可以创建小组，在project与student中都要创建记录
                projectFacade.addProject(newProjectId, name, null, null, headman, c);
                studentFacade.addProjectID(newProjectId, this.currentStudent, newCourseId);
                FacesMessage message = new FacesMessage("创建项目成功", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else if (p != null) {
                FacesMessage message = new FacesMessage("项目小组ID已存在，请更改");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage("课程ID不存在，请更改");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void lookStudents() {
        student.clear();
        student = studentFacade.findStudentsByPID(rowProject);
        System.out.println("lookStudent:" + rowProject);
        System.out.println("lookStudent find");
        System.out.println(student);
        /*
        for (Student s : studentFacade.findAll()) {
            System.out.println(s + ",  " + s.getProject() + ",  " + rowProject);
            if (s.getProject() != null) {
                if (rowProject.equals(s.getProject().getId())) {
                    student.add(s);
                }
            }
        }
         */
    }

    public List<Project> getProject() {
        project = projectFacade.findAll();
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    public List<Student> getStudent() {
//        student = studentFacade.findAll(); 
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public String getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(String currentStudent) {
        this.currentStudent = currentStudent;
    }

    public String getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(String currentProject) {
        this.currentProject = currentProject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalmark() {
        return totalmark;
    }

    public void setTotalmark(String totalmark) {
        this.totalmark = totalmark;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getCountProject() {
        return countProject;
    }

    public void setCountProject(int countProject) {
        projectFacade.countProject();
        this.countProject = countProject;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        System.out.println(selectedProject.getId());
        this.selectedProject = selectedProject;
    }

    public String getRowProject() {
        return rowProject;
    }

    public void setRowProject(String rowProject) {
        this.rowProject = rowProject;
    }

    public List<Project> getFilteredProjects() {
        return filteredProjects;
    }

    public void setFilteredProjects(List<Project> filteredProjects) {
        this.filteredProjects = filteredProjects;
    }

    /**
     * @return the newProjectId
     */
    public String getNewProjectId() {
        return newProjectId;
    }

    /**
     * @param newProjectId the newProjectId to set
     */
    public void setNewProjectId(String newProjectId) {
        this.newProjectId = newProjectId;
    }

    /**
     * @return the newCourseId
     */
    public String getNewCourseId() {
        return newCourseId;
    }

    /**
     * @param newCourseId the newCourseId to set
     */
    public void setNewCourseId(String newCourseId) {
        this.newCourseId = newCourseId;
    }

    /**
     * @return the oldProjectId
     */
    public String getOldProjectId() {
        return oldProjectId;
    }

    /**
     * @param oldProjectId the oldProjectId to set
     */
    public void setOldProjectId(String oldProjectId) {
        this.oldProjectId = oldProjectId;
    }

    /**
     * @return the oldCourseId
     */
    public String getOldCourseId() {
        return oldCourseId;
    }

    /**
     * @param oldCourseId the oldCourseId to set
     */
    public void setOldCourseId(String oldCourseId) {
        this.oldCourseId = oldCourseId;
    }
}
