/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.StudentFacade;

/**
 *
 * @author Dawson
 */
@Named(value = "fileDownloadController")
@SessionScoped
public class FileDownloadController implements Serializable {

    @EJB
    private StudentFacade studentFacade;

    private final String studentReportPath = "/studentReport/";
    private final String projectReportPath = "/projectReport/";
    private StreamedContent studentReport;
    private StreamedContent projectReport;
    private StreamedContent manager_studentReport;
    private StreamedContent manager_projectReport;

    ManagedBean managedBean;
    DDManagedBean dDManagedBean;
    HttpServletRequest request;
    private String realPath_projectReport;

    private Project selectProject;

    public FileDownloadController() {
        //两个托管bean之间的传参
        FacesContext context = FacesContext.getCurrentInstance();
        managedBean = (ManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "managedBean");
        dDManagedBean = (DDManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "dDManagedBean");
        request = (HttpServletRequest) context.getExternalContext().getRequest();
//        得到服务器相对地址
        realPath_projectReport = request.getRealPath("/projectReport/");
        System.out.println(managedBean.getStudentName());
    }

    public StreamedContent getStudentReport() throws FileNotFoundException {
        if (!"1".equals(managedBean.getStudent().getReport())) {
            FacesMessage message = new FacesMessage("下载失败", "您没有上传个人报告");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        String studentReportName = managedBean.getStudentId() + managedBean.getStudentName() + ".docx";
        System.out.println(studentReportName);
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(studentReportPath + studentReportName);
        studentReport = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "studentId:" + managedBean.getStudentId() + ".docx");
        return studentReport;
    }

    public StreamedContent getProjectReport() {
        Project p = managedBean.getProject();
        if (p == null) {
            FacesMessage message = new FacesMessage("下载失败", "您还没有参加项目");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else {
            String projectReportName = managedBean.getProject().getName() + "[组长" + managedBean.getProject().getHeadman() + "].zip";
            File file = new File(realPath_projectReport, projectReportName);
            System.out.println(realPath_projectReport + projectReportName);
            if (file.exists()) {
                InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(projectReportPath + projectReportName);
                projectReport = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "projectHeadman:" + managedBean.getProject().getHeadman() + ".zip");
                return projectReport;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("下载失败", "文件找不到，估计您的小组的项目报告没有上传"));
                return null;
            }
        }
    }

    public StreamedContent getManager_projectReport() {
        Project p = this.selectProject;
        if (p == null) {
            FacesMessage message = new FacesMessage("下载失败", "您还没有参加项目");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else {
            String projectReportName = p.getName() + "[组长" + p.getHeadman() + "].zip";
            File file = new File(realPath_projectReport, projectReportName);
            System.out.println(realPath_projectReport + projectReportName);
            if (file.exists()) {
                InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(projectReportPath + projectReportName);
                manager_projectReport = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "projectHeadman:" + p.getHeadman() + ".zip");
                return manager_projectReport;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("下载失败", "文件找不到，估计这个小组的项目报告没有上传"));
                return null;
            }
        }
    }

//    public StreamedContent getManager_studentReport() {
//        Student s = studentFacade.find(dDManagedBean.getSelectedStudent());
//        if (s != null && s.getReport().equals("1")) {
//            String studentReportName = s.getId() + s.getName() + ".docx";
//            System.out.println(studentReportName);
//            InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(studentReportPath + studentReportName);
//            manager_studentReport = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "studentId:" + managedBean.getStudentId() + ".docx");
//            return manager_studentReport;
//        } else {
//            FacesMessage message = new FacesMessage("下载失败", "该学生没有上传个人报告");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return null;
//        }
//    }

    public Project getSelectProject() {
        return selectProject;
    }

    public void setSelectProject(Project selectProject) {
        this.selectProject = selectProject;
    }

}
