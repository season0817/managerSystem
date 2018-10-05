/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Grade;
import entity.Student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.SelectEvent;
import session.GradeFacade;
import session.StudentFacade;

/**
 *
 * @author Dawson
 */
@ManagedBean(name = "editorViewThread")
@ViewScoped
public class EditorViewThread implements Serializable {

    HttpServletRequest request;
    private String text;
    private Student student;
    private String realPath;
    private EditorView editorView;
    private String chooseGradeName;
    private Grade chooseGrade;
    private List<Grade> grades;

    @EJB
    private StudentFacade studentFacade;
    @EJB
    private GradeFacade gradeFacade;

    /**
     * Creates a new instance of EditorViewThread
     */
    public EditorViewThread() {
        System.out.println("EditorViewThread()");
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        editorView = (EditorView) context.getApplication()
                .getVariableResolver().resolveVariable(context, "editorView");
        student = editorView.getStudent();
        text = student.getReport();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
//        得到服务器相对地址
        realPath = request.getRealPath("/template");
        grades = gradeFacade.findAll();
        System.out.println("EditorViewThread().init()");
    }

    public void readStudentReportTemplate() throws FileNotFoundException, UnsupportedEncodingException, IOException {
//         System.out.println(realPath);
        File file = new File(realPath, "/个人报告模板.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        text = null;
        text = br.readLine();
        while (br.ready()) {
            text += br.readLine();
        }
        fis.close();
    }

    public void readStudentReportFromMysql() {
        text = student.getReport();
    }

    public void saveStudentReportToMysql() {
        if (text.isEmpty()) {
            student.setReport(null);
        } else {
            student.setReport(text);
        }
       // student.setTotalMark(chooseGrade.getGrade());
        studentFacade.edit(student);
        RequestContext.getCurrentInstance().closeDialog("");
    }

    public void onSelected() {
//        UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
//        Editor editor = (Editor) root.findComponent("form:editor");
//        String content=(String) editor.getSubmittedValue();
//        System.out.println("onSelected()" + content);
        if (text == null || text.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "他并没有填写个人报告", ""));
            chooseGrade = null;
            chooseGradeName = null;
        } else {
            chooseGrade = gradeFacade.find(chooseGradeName);
            StringBuilder textBuffer = new StringBuilder(text);
            int index = textBuffer.indexOf("评语");
            if (index > 0 && textBuffer.indexOf("成绩（百分制）：") > 0) {
                int index1 = textBuffer.indexOf("</b>", index);
//                先删除原来的评论内容，再插入评论
                System.out.println(index + ",   " + index1);
                textBuffer.delete(index + 2, index1);
                textBuffer.insert(index + 2, "<br/>" + chooseGrade.getName() + "<br/>" + chooseGrade.getComment());
                index = textBuffer.indexOf("成绩（百分制）：");
                if (index > 0) {
                    index1 = textBuffer.indexOf("</b>", index);
                    //   先删除原来的成绩内容，再插入成绩
                    textBuffer.delete(index + 8, index1);
                    textBuffer.insert(index + 8, chooseGrade.getGrade());
                    this.text = textBuffer.toString();
                }
            } else {
                chooseGradeName = null;
                chooseGrade = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "找不到关键字不能自动评论，请手动评论", "'评语'和'成绩（百分制）：'"));
            }
            RequestContext.getCurrentInstance().update("form:growl,form:editor,form:listbox");
            System.out.println(index + ",   ");
        }
    }

    public void valueChange() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "保存草稿成功", ""));
        System.out.println("valueChange()" + text);
        //           程序模仿鼠标点击事件
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Grade getChooseGrade() {
        return chooseGrade;
    }

    public void setChooseGrade(Grade chooseGrade) {
        this.chooseGrade = chooseGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public String getChooseGradeName() {
        return chooseGradeName;
    }

    public void setChooseGradeName(String chooseGradeName) {
        this.chooseGradeName = chooseGradeName;
    }

}
