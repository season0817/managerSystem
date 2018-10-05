/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Project;
import entity.ProjectSchedule;
import entity.Student;
import entity.StudentSchedule;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import session.ProjectFacade;
import session.ProjectScheduleFacade;
import session.StudentFacade;
import session.StudentScheduleFacade;
import javax.faces.bean.ManagedBean;
import org.apache.poi.ss.usermodel.CellType;
/**
 *
 * @author Dawson
 */
@ManagedBean(name = "dDManagedBean")
@ViewScoped
public class DDManagedBean implements Serializable {

    private List<Student> students = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Project> projectsMenu = new ArrayList<>();
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private ProjectScheduleFacade projectScheduleFacade;
    @EJB
    private StudentScheduleFacade studentScheduleFacade;

    private Student chooseStudent;

    private String projectSchedule;
    private String studentSchedule;
    private String selectedStudent2;
    private String selectedProject2;

    private String selectedStudent;
    private String selectedProject;
    private String selectedProjectName;
    private String stu_id;
    private String stu_name;
    private String pro_name;
    private String pro_headman;
    private List<Student> filteredStudents;
    private List<Project> filteredProjects;
    private List<Student> lookStudents = new ArrayList<>();
    HttpServletRequest request;
    String realPath;

    /**
     * Creates a new instance of DDManagedBean
     */
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        realPath = this.request.getRealPath("/externalStudentsData");
        projects = projectFacade.findAll();
        students = studentFacade.findAll();
    }

//    编辑功能
    public String onEdit(RowEditEvent event) {
        FacesMessage msg;
        chooseStudent = (Student) event.getObject();
//      如果要为项目负责人更改他参与的项目 则终止该方法，等待用户做决定
        if (chooseStudent.getProject() != null
                && chooseStudent.getProject().getHeadman().equals(chooseStudent.getStudentPK().getId())
                && chooseStudent.getProject().getId().compareTo(selectedProject) != 0) {
            RequestContext.getCurrentInstance().execute("PF('cd').show()");
            return "";
        }
        if (studentFacade.changeStudentView(chooseStudent, selectedProject)) {
            msg = new FacesMessage("Student Edited", chooseStudent.getStudentPK().getId());
//             刷新this.students中的数据
            selectedProject = null;
        } else {
            msg = new FacesMessage("Student Can't Edited", chooseStudent.getStudentPK().getId());
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.updateData();
        return "";
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Student Cancelled", ((Student) event.getObject()).getStudentPK().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onEdit2(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if (!newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            projectFacade.edit(projects.get(event.getRowIndex()));
            this.updateData();
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if (!newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            studentFacade.edit(lookStudents.get(event.getRowIndex()));
            this.updateData();
        }
    }

    public void removeHeadMan() {
        if (studentFacade.changeStudentView(chooseStudent, selectedProject)) {
            //清空没有负责人的项目
            for (Project project : projectFacade.findAll()) {
                Student s = studentFacade.find(project.getHeadman());
//              如果原本的项目负责人没有了项目，或者他参与的新项目与原本项目不符合
                if (s.getProject() == null || !project.equals(s.getProject())) {
                    this.removeProject(project.getId());
                    RequestContext.getCurrentInstance().execute("window.location.reload();alert(\"一位项目负责人参与的项目被更改，所以它负责的项目被删除了\");");
                }
            }
//          刷新this.students中的数据
            selectedProject = null;
            updateData();
        }
    }

    public void removeStudent(Student s) {
        System.out.println("web.DDManagedBean.removeStudent()");
        FacesMessage msg;
        if (s == null) {
            msg = new FacesMessage("删除操作失败", "在数据库中未找到StudentID：" + s.getStudentPK().getId());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (s.getProject() == null) {
            //没有项目的学生
            studentFacade.dropStudent(s.getStudentPK());
            msg = new FacesMessage("删除成功", "已经删除没有项目学生");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (s.getStudentPK().getId().equals(s.getProject().getHeadman())) {
            //有项目并且是负责人
            projectFacade.removeProject(s.getProject().getId());
            studentFacade.dropStudent(s.getStudentPK());
            msg = new FacesMessage("删除成功", "已经删除学生及其负责的项目");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            //有项目但不是是负责人  
            studentFacade.dropStudent(s.getStudentPK());
            msg = new FacesMessage("删除成功", "已经删除有项目但不是负责人的学生");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //  刷新this.students中的数据
//        students=studentFacade.findAll();
        this.updateData();
    }

    public void removeProject(String projectID) {
        List<Student> students = studentFacade.findAll();
        for (Student s : students) {
            if (s.getProject() != null) {
                if (s.getProject().getId() == projectID) {
                    s.setProject(null);
                    studentFacade.edit(s);
                }
            }
        }
        Project p = projectFacade.find(projectID);
        projectFacade.remove(p);
//        对this.project进行更新 并对数据库的表student进行更新，并对dDManagedBean页面进行更新
        this.updateData();
//                并对lookStudents进行更新
        lookStudents.clear();
//        List<Student> s=sf.findAll();
//        for(Student sd: s){
//            System.out.println(sd.getId()+sd.getProjectID());
//        }
        FacesMessage msg = new FacesMessage("已经删除项目", "项目负责人：" + projectID + "\n项目名称：" + p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void addStudent() {
        //需要projectid
        if (stu_id.isEmpty() || stu_id == null || stu_name.isEmpty() || stu_name == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("添加失败,姓名学号都不能为空", ""));
        } else {
            Student s = studentFacade.find(stu_id);
            if (s == null) {
                FacesMessage msg = new FacesMessage("添加成功", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                s = new Student();
                //s.setId(stu_id);
                s.setName(stu_name);
                studentFacade.create(s);
                //  刷新this.students中的数据
                updateData();
            } else {
                FacesMessage msg = new FacesMessage("添加失败", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
        stu_id = null;
        stu_name = null;
    }

    public void addProject() {
        //不能用headman的学号作为项目id,可以尝试让学生创建projectid
//        这里会令headman等于项目的id
        if (pro_headman == null ||pro_headman.isEmpty()  || pro_name == null || pro_name.isEmpty() ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("添加失败,项目负责人和项目名称都不能为空", ""));
        } else {
            Student s = studentFacade.find(pro_headman);
            if (s == null) {
                FacesMessage msg = new FacesMessage("添加失败", "没有该学生");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (s.getProject() != null) {
                FacesMessage msg = new FacesMessage("添加失败", "该学生已经参与有项目");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                Project p = projectFacade.find(pro_headman);
                if (p == null) {
                    p = new Project();
                    p.setId(pro_headman);
                    p.setName(pro_name);
                    p.setHeadman(pro_headman);
                    projectFacade.create(p);
                    s.setProject(p);
                    studentFacade.edit(s);
                    //  刷新this.students和this.projects中的数据
                    updateData();
                } else {
                    FacesMessage msg = new FacesMessage("出错", "该项目已经存在，请联系技术人员");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        }
        pro_headman = null;
        pro_name = null;
    }

    public void lookupStudents() {
        System.out.println(this.selectedProject);
        lookStudents = studentFacade.lookStudents(this.selectedProject);
    }

    public void updateData() {
        //students.clear();
        students = studentFacade.findAll();
        this.filteredStudents = students;
        projects = projectFacade.findAll();
        this.filteredProjects=projects;
        selectedProject=null;
        for (Student s : students) {
           System.out.println(s.getStudentPK().getId() + ":" + s.getProject());
      }
    }

    /**
     * 读取Excel测试，兼容 Excel 2003/2007/2010
     */
    public void readExcel() {
        List<Student> list = studentFacade.findAll();
        for (Student s : list) {
            this.removeStudent(s);
//           studentFacade.remove(s);
        }
        Student student;
        try {
            //同时支持Excel 2003、2007  
            File excelFile = new File(realPath, "/students.xlsx"); //创建文件对象  
            FileInputStream is = new FileInputStream(excelFile); //文件流  
            Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的  
            int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
            //遍历每个Sheet  
            for (int s = 0; s < sheetCount; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
                //遍历每一行  
                for (int r = 0; r < rowCount; r++) {
                    student = new Student();
                    Row row = sheet.getRow(r);
                    int cellCount = row.getPhysicalNumberOfCells(); //获取总列数  
                    //遍历所有列  
                    for (int c = 0; c < cellCount; c++) {
                        Cell cell = row.getCell(c);
                        //cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.toString();;
                        System.out.print(cellValue + "");
                        if (c == 0) {
                            //添加学生学号到记录
                            //需要一个projectid
                            //student.setUserstudent(userstudent);
                            //student.setId(cellValue);
                        } else if (c == 1) {
                            student.setName(cellValue);
                        }
                    }
                    if (this.studentFacade.find(student.getStudentPK().getId()) == null) {
                        this.studentFacade.create(student);
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  刷新this.students中的数据
        updateData();
    }

    public void readProjectScheduleFromMysql() {
        this.projectSchedule = "";
        for (ProjectSchedule ps : projectScheduleFacade.findByProjectID(this.selectedProject2)) {
            //先用amconten代替原来的
            this.projectSchedule += ps.getAMContent() + "<br />";
        }
    }

    public void readStudentScheduleFromMysql() {
        this.studentSchedule = "";
        for (StudentSchedule ss : studentScheduleFacade.findByStudentID(this.selectedStudent2)) {
            this.studentSchedule += ss.getAMContent()+ss.getPMContent() + "<br />";
        }
    }
    public void editStudentReportReturn(){
        this.updateData();
        this.lookupStudents();
        System.out.println("editStudentReportReturn()");
    }

    public List<Student> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(List<Student> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(String selectedProject) {
        this.selectedProject = selectedProject;
    }

    public String getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(String selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public List<Student> getStudents() {
//        if (students.isEmpty()) {
//            students = studentFacade.findAll();
//        }
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Project> getProjects() {

        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_headman() {
        return pro_headman;
    }

    public void setPro_headman(String pro_headman) {
        this.pro_headman = pro_headman;
    }

    public List<Student> getLookStudents() {
        return lookStudents;
    }

    public void setLookStudents(List<Student> lookStudents) {
        this.lookStudents = lookStudents;
    }

    public List<Project> getProjectsMenu() {
        Project p = new Project();
        String id = null;
        p.setId(id);
        p.setName("不选择项目");
        projectsMenu.clear();
        projectsMenu.add(p);
        for (Project pro : this.getProjects()) {
            projectsMenu.add(pro);
        }
        return projectsMenu;
    }

    public void setProjectsMenu(List<Project> projectsMenu) {
        this.projectsMenu = projectsMenu;
    }

    public String getSelectedProjectName() {
        return selectedProjectName;
    }

    public void setSelectedProjectName(String selectedProjectName) {
        this.selectedProjectName = selectedProjectName;
    }

    public List<Project> getFilteredProjects() {
        return filteredProjects;
    }

    public void setFilteredProjects(List<Project> filteredProjects) {
        this.filteredProjects = filteredProjects;
    }

    public String getProjectSchedule() {
        return projectSchedule;
    }

    public void setProjectSchedule(String projectSchedule) {
        this.projectSchedule = projectSchedule;
    }

    public String getStudentSchedule() {
        return studentSchedule;
    }

    public void setStudentSchedule(String studentSchedule) {
        this.studentSchedule = studentSchedule;
    }

    public String getSelectedStudent2() {
        return selectedStudent2;
    }

    public void setSelectedStudent2(String selectedStudent2) {
        this.selectedStudent2 = selectedStudent2;
    }

    public String getSelectedProject2() {
        return selectedProject2;
    }

    public void setSelectedProject2(String selectedProject2) {
        this.selectedProject2 = selectedProject2;
    }

}
