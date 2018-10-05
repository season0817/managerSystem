/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.Project;
import entity.Student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.primefaces.context.RequestContext;
import session.ProjectFacade;
import session.StudentFacade;
//import org.apache.commons.io.FileUtils;

/**
 *
 * @author Dawson
 */
@Named(value = "editorView")
@SessionScoped
public class EditorView implements Serializable {

    /**
     * Creates a new instance of EditorView
     */
    web.ManagedBean managedBean;
    web.DDManagedBean dDManagedBean;
    HttpServletRequest request;
    EditorViewThread editorViewThread;
    private String text;

    private Student student;
    private Project project;
    private String realPath;
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private ProjectFacade projectFacade;

    public EditorView() {
        FacesContext context = FacesContext.getCurrentInstance();
        managedBean = (web.ManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "managedBean");
        dDManagedBean = (web.DDManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "dDManagedBean");
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        realPath = request.getRealPath("/template");
        System.out.println("EditorView()");
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getText() throws IOException {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String newReport() throws UnsupportedEncodingException, IOException {
        if (student.getReport() == null) {
            this.readTemplate();
        } else {
            text = student.getReport();
        }
        return "reportEditor";
    }

    public void readTemplate() throws FileNotFoundException, UnsupportedEncodingException, IOException {
//         System.out.println(realPath);
        File file = new File(realPath, "/个人报告模板.txt");
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
        br.close();
        fis.close();
    }

    public void readProjectReportTemplate() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File file = new File(realPath, "/项目报告模板.txt");
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

    public void readFromMysql() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        text = student.getReport();
    }

    public void readStudentReportFromMysql() {
        text = student.getReport();
    }

    public void readProjectReportFromMysql() {
        text = project.getReport();
    }

    public void saveToMysql() {
        if (text.isEmpty()) {
            student.setReport(null);
        } else {
            student.setReport(text);
        }
        studentFacade.edit(student);
    }

    public void saveStudentReportToMysql() {
        if (text.isEmpty()) {
            student.setReport(null);
        } else {
            student.setReport(text);
        }
        studentFacade.edit(student);
        RequestContext.getCurrentInstance().closeDialog("");

    }

    public void saveProjectReportToMysql() {
        if (text.isEmpty()) {
            project.setReport(null);
        } else {
            project.setReport(text);
        }

        projectFacade.edit(project);
        RequestContext.getCurrentInstance().closeDialog("");
    }

    public String studentEdit() {
        student = studentFacade.find(this.managedBean.getStudentId());
        return "reportEditor.xhtml";
    }

    public void onReturn() {
//        this.dDManagedBean.editStudentReportReturn();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您已成功修改个人报告", student.getStudentPK().getId() + student.getName()));
    }

    public void onReturn2() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您已成功修改项目报告", project.getId() + project.getName()));
    }

    public void showStudentReportEditDialog() throws UnsupportedEncodingException, IOException {
        /*需要添加projectId来查找student*/
        student = studentFacade.findStudent(this.managedBean.getStudentId(),this.managedBean.getProjectId());
        text = student.getReport();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 500);
        options.put("contentWidth", 900);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editor_studentReport", options, null);
    }

    public void showStudentReportEditDialog2() throws UnsupportedEncodingException, IOException {
        text = student.getReport();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 500);
        options.put("contentWidth", 900);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editor_studentReport", options, null);
    }

    public void showStudentReportEditDialog3() {
        text = student.getReport();
        System.out.println("showStudentReportEditDialog3()");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("height", 500);
        options.put("contentWidth", 900);
        RequestContext.getCurrentInstance().openDialog("editor_studentReportThread", options, null);
    }

    public void showProjectReportEditDialog() {
        try {
            //需要projectid
             student = studentFacade.findStudent(this.managedBean.getStudentId(),this.managedBean.getProjectId());
            project = student.getProject();
            text = project.getReport();
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("modal", true);
            options.put("height", 500);
            options.put("contentWidth", 900);
            options.put("resizable", false);
            RequestContext.getCurrentInstance().openDialog("editor_projectReport", options, null);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "尚未参与项目", ""));
        }
    }

    public void showProjectReportEditDialog2() {
        try {
            text = project.getReport();
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("modal", true);
            options.put("height", 500);
            options.put("contentWidth", 900);
            options.put("resizable", false);
            RequestContext.getCurrentInstance().openDialog("editor_projectReport", options, null);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "尚未参与项目", ""));
        }
    }

    public void saveToHtml() throws IOException, Exception {
        System.out.println(text);
        FileUtils.writeStringToFile(new File("d:\\save4.html"), text, "utf-8");
        htmlToWord();
    }

    public void htmlToWord() throws Exception {
        //拼一个标准的HTML格式文档
        String content = text;
        InputStream is = new ByteArrayInputStream(content.getBytes("utf-8"));
        OutputStream os = new FileOutputStream("d:\\1.doc");
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

//    public void writeFile(String content, String path) {
//        FileOutputStream fos = null;
//        BufferedWriter bw = null;
//        try {
//            File file = new File(path);
//            fos = new FileOutputStream(file);
//            bw = new BufferedWriter(new OutputStreamWriter(fos, "GB2312"));
//            bw.write(content);
//        } catch (FileNotFoundException fnfe) {
//            fnfe.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
//            try {
//                if (bw != null) {
//                    bw.close();
//                }
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException ie) {
//            }
//        }
//    }
//    public void save2() throws TransformerException, IOException, ParserConfigurationException {
//        convert2Html("D://个人报告.doc", "D://save2.html");
//    }
//
//    public void convert2Html(String fileName, String outPutFile)
//            throws TransformerException, IOException,
//            ParserConfigurationException {
//        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));  
//        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
//                DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                        .newDocument());
//        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
//            public String savePicture(byte[] content,
//                    PictureType pictureType, String suggestedName,
//                    float widthInches, float heightInches) {
//                return "test/" + suggestedName;
//            }
//        });
//        wordToHtmlConverter.processDocument(wordDocument);
//        //save pictures  
//        List pics = wordDocument.getPicturesTable().getAllPictures();
//        if (pics != null) {
//            for (int i = 0; i < pics.size(); i++) {
//                Picture pic = (Picture) pics.get(i);
//                System.out.println();
//                try {
//                    pic.writeImageContent(new FileOutputStream("D:/test/"
//                            + pic.suggestFullFileName()));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        Document htmlDocument = wordToHtmlConverter.getDocument();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        DOMSource domSource = new DOMSource(htmlDocument);
//        StreamResult streamResult = new StreamResult(out);
//
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer serializer = tf.newTransformer();
//        serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
//        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//        serializer.setOutputProperty(OutputKeys.METHOD, "html");
//        serializer.transform(domSource, streamResult);
//        out.close();
//        writeFile(new String(out.toByteArray()), outPutFile);
//        text = out.toString();
//    }
//    public void save4() throws Throwable {
//        final String path = "D:\\";
//        final String file = "个人报告.doc";
//        InputStream input = new FileInputStream(path + file);
//        HWPFDocument wordDocument = new HWPFDocument(input);
//        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
//                DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                        .newDocument());
//        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
//            public String savePicture(byte[] content, PictureType pictureType,
//                    String suggestedName, float widthInches, float heightInches) {
//                return suggestedName;
//            }
//        });
//        wordToHtmlConverter.processDocument(wordDocument);
//        List pics = wordDocument.getPicturesTable().getAllPictures();
//        if (pics != null) {
//            for (int i = 0; i < pics.size(); i++) {
//                Picture pic = (Picture) pics.get(i);
//                try {
//                    pic.writeImageContent(new FileOutputStream(path
//                            + pic.suggestFullFileName()));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        Document htmlDocument = wordToHtmlConverter.getDocument();
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        DOMSource domSource = new DOMSource(htmlDocument);
//        StreamResult streamResult = new StreamResult(outStream);
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer serializer = tf.newTransformer();
//        serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
//        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//        serializer.setOutputProperty(OutputKeys.METHOD, "html");
//        serializer.transform(domSource, streamResult);
//        outStream.close();
//        String content = new String(outStream.toByteArray());
//        FileUtils.writeStringToFile(new File(path, "save4.html"), content, "GB2312");
//        text=content;
//    }
//    public void docToHtml() throws Exception {
//        String sourceFileName = "d:\\个人报告.doc";
//        String targetFileName = "d:\\test.html";
//        String imagePathStr = "d:\\";
//        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
//        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
//        // 保存图片，并返回图片的相对路径 
//        wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
//            try (FileOutputStream out = new FileOutputStream(imagePathStr + name)) {
//                out.write(content);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "image/" + name;
//        });
//        wordToHtmlConverter.processDocument(wordDocument);
//        Document htmlDocument = wordToHtmlConverter.getDocument();
//        DOMSource domSource = new DOMSource(htmlDocument);
//        StreamResult streamResult = new StreamResult(new File(targetFileName));
//
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer serializer = tf.newTransformer();
//        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
//        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//        serializer.setOutputProperty(OutputKeys.METHOD, "html");
//        serializer.transform(domSource, streamResult);
//
//        File file = new File(targetFileName);
//        FileInputStream fis = new FileInputStream(file);
//        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
//        while (br.ready()) {
//            text += br.readLine();
//        }
//        fis.close();
//    }
//    
}
