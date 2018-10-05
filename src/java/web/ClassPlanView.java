/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.Serializable;
import java.util.List;
import entity.ClassPlan;
import entity.Course;
import entity.Grade;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.ClassPlanFacade;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import session.CourseFacade;
import session.GradeFacade;

/**
 *
 * @author Dawson
 */
@ManagedBean(name = "classPlanView")
@ViewScoped
public class ClassPlanView implements Serializable {

    private String text;
    private List<Grade> grades;
    private List<ClassPlan> classPlans;
    @EJB
    private ClassPlanFacade classPlanFacade;
    @EJB
    private GradeFacade gradeFacade;

    private DDManagedBean dDManagedBean;
    private HttpServletRequest request;
    private String realPath;
    private String studentsDataPath;
    private Grade grade;
   

    @PostConstruct
    public void init() {
         FacesContext context = FacesContext.getCurrentInstance();    
        classPlans = classPlanFacade.findAll();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        realPath = request.getRealPath("/template/");
        studentsDataPath = request.getRealPath("/externalStudentsData");
        dDManagedBean = (DDManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "dDManagedBean");
        grades=gradeFacade.findAll();
        grade=new Grade();
        System.out.println(" ClassPlanView.init()"+grades);
    }

   
    
    public void editProjectReport() {
        this.showDialog("change_projectReport");
    }

    public void editStudentReport() {
        this.showDialog("change_studentReport");
    }

    public void editScheduleStudent() {
        this.showDialog("change_scheduleStudent");
    }

    public void editScheduleProject() {
        this.showDialog("change_scheduleProject");
    }

    private void showDialog(String xhtml) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("height", 500);
        options.put("contentWidth", 900);
        RequestContext.getCurrentInstance().openDialog(xhtml, options, null);
    }

    public void readProjectReportTemplate() throws FileNotFoundException, IOException {
        this.readTemplate("项目报告模板.txt");
    }

    public void saveAsProjectReportTemplate() throws FileNotFoundException, IOException {
        this.saveAsTemplate("项目报告模板.txt");
        RequestContext.getCurrentInstance().closeDialog("");
    }

    public void readStudentReportTemplate() throws FileNotFoundException, IOException {
        this.readTemplate("个人报告模板.txt");
    }

    public void saveAsStudentReportTemplate() throws FileNotFoundException, IOException {
        this.saveAsTemplate("个人报告模板.txt");
        RequestContext.getCurrentInstance().closeDialog("");
    }

    public void readScheduleStudentTemplate() throws IOException {
        this.readTemplate("个人进度模板.txt");
    }

    public void saveAsScheduleStudentTemplate() throws IOException {
        this.saveAsTemplate("个人进度模板.txt");
        RequestContext.getCurrentInstance().closeDialog("");
    }

    public void readScheduleProjectTemplate() throws IOException {
        this.readTemplate("项目进度模板.txt");
    }

    public void saveAsScheduleProjectTemplate() throws IOException {
        this.saveAsTemplate("项目进度模板.txt");
        RequestContext.getCurrentInstance().closeDialog("");
    }

    private void readTemplate(String filename) throws FileNotFoundException, IOException {
        File file = new File(realPath, filename);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        text = null;
        text = br.readLine();
        while (br.ready()) {
            text += br.readLine();
        }
        br.close();
        fis.close();
    }

    private void saveAsTemplate(String filename) throws FileNotFoundException, IOException {
        File file = new File(realPath, filename);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(text, 0, text.length());
        bw.close();
        fos.close();
    }

    public void uploadStudentsInfo(FileUploadEvent event) throws FileNotFoundException, IOException {
        FacesMessage message = new FacesMessage("学生数据上传更新成功");
        FacesContext.getCurrentInstance().addMessage(null, message);
        InputStream is = new BufferedInputStream(event.getFile().getInputstream());
        byte[] buffer = new byte[(int) event.getFile().getSize()];
//        得到相对路径
        System.out.println(studentsDataPath);
        File f = new File(studentsDataPath, "students.xlsx");
        FileOutputStream fos = new FileOutputStream(f);
        while (is.read(buffer) > 0) {
            fos.write(buffer);
        }
        System.out.println("写入成功");
        fos.close();
        dDManagedBean.readExcel();
        RequestContext.getCurrentInstance().closeDialog("");
        RequestContext.getCurrentInstance().execute("window.location.reload();alert(\"学生数据已经更新完成\");");
    }

    public void editStudentsData() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("height", 500);
        options.put("contentWidth", 900);
        options.put("contentHeight", 450);
        RequestContext.getCurrentInstance().openDialog("change_studentsData", options, null);
    }

    public void onEdit(RowEditEvent event) {
        ClassPlan classPlan = (ClassPlan) event.getObject();
        classPlanFacade.edit(classPlan);
    }

    public void onCancel() {

    }
    
    public void onGradesEdit(RowEditEvent event){
        Grade grade=(Grade)event.getObject();
        gradeFacade.edit(grade);
    }
    
    public void addGrade(){
        Grade grade=this.gradeFacade.find(this.grade.getName());
        if(grade == null){
            this.gradeFacade.create(this.grade);
            this.grades=this.gradeFacade.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"评语创建成功",""));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"评语创建失败","已经有相同名称的评语"));
        }
    }
    
    public void removeGrade(Grade grade){
            this.gradeFacade.remove(grade);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"删除成功",""));
            this.grades=this.gradeFacade.findAll();
    }

    public List<ClassPlan> getClassPlans() {
        return classPlans;
    }
    

    public void setClassPlans(List<ClassPlan> classPlans) {
        this.classPlans = classPlans;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }



    public GradeFacade getGradeFacade() {
        return gradeFacade;
    }

    public void setGradeFacade(GradeFacade gradeFacade) {
        this.gradeFacade = gradeFacade;
    }

 

     
    
}
