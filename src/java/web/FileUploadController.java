/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import session.ProjectFacade;
import session.StudentFacade;

/**
 *
 * @author Dawson
 */
@javax.faces.bean.ManagedBean(name = "fileUploadController")
@ViewScoped
public class FileUploadController implements Serializable {

    private String studentReportPath;
    private String projectReportPath;

    /**
     * Creates a new instance of FileUploadController
     */
    private UploadedFile student_file;
    private UploadedFile project_file;
//    private UploadedFile projetFileUpload;
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ProjectFacade projectFacade;
    private String studentsDataPath;

    ManagedBean managedBean;
    HttpServletRequest request;

    @PostConstruct
    public void init() {
        //两个托管bean之间的传参
        FacesContext context = FacesContext.getCurrentInstance();
        managedBean = (ManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "managedBean");
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        studentsDataPath = request.getRealPath("/externalStudentsData");
        projectReportPath = request.getRealPath("/projectReport");
        System.out.println(projectReportPath);
    }

    public void studentFileUpload(FileUploadEvent event) throws IOException {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        student_file = event.getFile();
        InputStream is = new BufferedInputStream(student_file.getInputstream());
        byte[] buffer = new byte[(int) student_file.getSize()];

//        得到相对路径
        studentReportPath = request.getRealPath("/studentReport");
        System.out.println(studentReportPath);
        File f = new File(studentReportPath, managedBean.getStudentId() + managedBean.getStudentName() + ".docx");
        FileOutputStream fos = new FileOutputStream(f);
        while (is.read(buffer) > 0) {
            fos.write(buffer);
        }
        studentFacade.studentReportUploaded(managedBean.getStudentId());
        System.out.println("写入成功");
        fos.close();
    }

     public void projectFileUpload(FileUploadEvent event) throws IOException {
        if (managedBean.getProject() == null) {
            FacesMessage message = new FacesMessage("注意", "您还没有参加项目");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (!managedBean.getStudentId().equals(managedBean.getProject().getHeadman())) {
            FacesMessage message = new FacesMessage("注意", "您没有成为项目的组长，不能上传项目报告");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            project_file = event.getFile();
            InputStream is = new BufferedInputStream(project_file.getInputstream());
            byte[] buffer = new byte[(int) project_file.getSize()];
            File f = new File(projectReportPath, managedBean.getProject().getName() + "[组长" + managedBean.getProject().getHeadman() + "].zip");
            FileOutputStream fos = new FileOutputStream(f);
            while (is.read(buffer) > 0) {
                fos.write(buffer);
            }
//            projectFacade.projectReportUploaded(managedBean.getProject().getId());
            System.out.println("上传项目文件成功");
            fos.close();
        }
    }

    public UploadedFile getStudent_file() {
        return student_file;
    }

    public void setStudent_file(UploadedFile student_file) {
        this.student_file = student_file;
    }

    public UploadedFile getProject_file() {
        return project_file;
    }

    public void setProject_file(UploadedFile project_file) {
        this.project_file = project_file;
    }

    public String getStudentReportPath() {
        return studentReportPath;
    }

    public void setStudentReportPath(String studentReportPath) {
        this.studentReportPath = studentReportPath;
    }

    public String getProjectReportPath() {
        return projectReportPath;
    }

    public void setProjectReportPath(String projectReportPath) {
        this.projectReportPath = projectReportPath;
    }

}
