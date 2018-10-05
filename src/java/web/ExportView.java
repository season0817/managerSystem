/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Project;
//import entity.Solution;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;

import entity.Student;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.ProjectFacade;
//import session.SolutionFacade;
import session.StudentFacade;

import org.apache.poi.xslf.usermodel.SlideLayout;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author Dawson
 */
@Named(value = "exportView")
@SessionScoped
public class ExportView implements Serializable {

    private String text;
    private StreamedContent allStudentsReports;
    private StreamedContent allProjectsReports;
    private StreamedContent studentSolutions;
    //@EJB
    //private SolutionFacade solutionFacade;
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ProjectFacade projectFacade;

    HttpServletRequest request;
    String realpath;

    public ExportView() {
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        realpath = request.getRealPath("/studentReport");
    }

    public StreamedContent getSolution(String studentId) throws FileNotFoundException, IOException {
        System.out.println(studentId);
        text = "";
        XMLSlideShow ppt = new XMLSlideShow();
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
        XSLFSlideLayout titleLaout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        XSLFSlide slide = ppt.createSlide(titleLaout);
        XSLFTextShape title = slide.getPlaceholder(0);
        title.setText("test title");
        XSLFTextShape body = slide.getPlaceholder(1);
        body.clearText();
        body.addNewTextParagraph().addNewTextRun().setText("test body");
        File file = new File(realpath + "/test.pptx");
        try (FileOutputStream out = new FileOutputStream(file)) {
            ppt.write(out);
            out.close();
            InputStream stream = new FileInputStream(realpath + "/test.pptx");
            allProjectsReports = new DefaultStreamedContent(stream);
        }
        return studentSolutions;
    }

    public StreamedContent getAllProjectsReports() throws Exception {
        //拼一个标准的HTML格式文档  
        text = "";
        for (Project p : projectFacade.findAll()) {
            if (p.getReport() != null) {
//                 word中的分页代码，打印时候就会呈现其价值
//这里不添加分页代码了，制作模板的时候自带了分页代码
                text += p.getReport();
            }
        }
        String content = "<html><head><style>" + "</style></head><body>" + text + "</body></html>";
        InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));

        if (realpath != null) {
            OutputStream os = new FileOutputStream(realpath + "/AllProjectsReports.doc");
            POIFSFileSystem fs = new POIFSFileSystem();
            //对应于org.apache.poi.hdf.extractor.WordDocument  
            fs.createDocument(is, "WordDocument");
            fs.writeFilesystem(os);
            os.close();
            is.close();

            InputStream stream = new FileInputStream(realpath + "/AllProjectsReports.doc");
            allProjectsReports = new DefaultStreamedContent(stream, "application/msword", "AllProjectsReports.doc");
            return allProjectsReports;
        } else {
            return null;
        }
    }

    public String readAllReports() {
        text = "";
        for (Student s : studentFacade.findAll()) {
            if (s.getReport() != null) {
//                 print中的分页代码，打印时候就会呈现其价值
                text += s.getReport() + "<div STYLE=\"page-break-after: always;\"/>";
            }
        }
        return "allReportsEditor";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
