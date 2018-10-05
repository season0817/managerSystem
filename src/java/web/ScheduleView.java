/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entity.ClassPlan;
import entity.Course;
import entity.CourseSchedule;
import entity.Project;
import entity.ProjectSchedule;
import entity.Student;
import entity.StudentSchedule;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import session.ClassPlanFacade;
import session.CourseFacade;
import session.CourseScheduleFacade;
import session.ProjectFacade;
import session.ProjectScheduleFacade;
import session.StudentFacade;
import session.StudentScheduleFacade;

@Named(value = "scheduleView")
@SessionScoped
public class ScheduleView implements Serializable {

    private ScheduleModel eventModel = new DefaultScheduleModel();
    private ScheduleModel pmEventModel = new DefaultScheduleModel();

    private DefaultScheduleEvent event = new DefaultScheduleEvent();
    private String text;

    private Date startDate;
    private Date endDate;
    private Date selectDate;
    private ClassPlan classPlan;
    private Student student;
    private Project project;
    private Course course;

    private String projectId;
    
    HttpServletRequest request;
    String realpath;
    web.ManagedBean managedBean;

    @EJB
    private ClassPlanFacade classPlanFacade;
    @EJB
    private StudentFacade studentFacade;
    @EJB
    private CourseFacade courseFacade;
    @EJB
    private StudentScheduleFacade studentScheduleFacade;
    @EJB
    private CourseScheduleFacade courseScheduleFacade;
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private ProjectScheduleFacade projectScheduleFacade;

//    @PostConstruct
//    public void init() {
    public ScheduleView() {
//        classPlan = classPlanFacade.find("1");
//        startDate = classPlan.getStartDate();
//        endDate = classPlan.getEndDate();
//        selectDate = classPlan.getStartDate();
//        eventModel = new DefaultScheduleModel();
//        eventModel.addEvent(new DefaultScheduleEvent("JAVAEE课程实践设计", startDate, endDate, true));
//        FacesContext context = FacesContext.getCurrentInstance();
//        managedBean = (web.ManagedBean) context.getApplication()
//                .getVariableResolver().resolveVariable(context, "managedBean");

//        System.out.println("ScheduleView.init():" + startDate + ",   " + endDate);
//        if ("student_welcome".equals(managedBean.studentLogin())) {
//            student = studentFacade.find(managedBean.getStudentId());
////            List<StudentSchedule> ssList = studentScheduleFacade.findByStudentID(student.getId());
////            for (StudentSchedule ss : ssList) {
////                eventModel.addEvent(new DefaultScheduleEvent("已填", ss.getStudentSchedulePK().getDate(), ss.getStudentSchedulePK().getDate(), true));
////            }
//            this.newStudentSchedule();
//        } else if ("manager_welcome".equals(managedBean.managerLogin())) {
//            project = projectFacade.find(managedBean.getManagerId());
//            this.newProjectSchedule();
//        }
//        System.out.println("init()");
    }

    public void onEventSelect(SelectEvent selectEvent) {
//        event = (DefaultScheduleEvent) selectEvent.getObject();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请点击日期下方的空白区域", ""));
    }

    public void onAMDateSelect(SelectEvent selectEvent) {
        selectDate = (Date) selectEvent.getObject();
        if ((selectDate.after(startDate) || selectDate.toString().equals(startDate.toString()))
                && (selectDate.before(endDate) || selectDate.toString().equals(endDate.toString()))) {
            if (selectDate.before(today1am())) {
//                if (selectDate.toString().equals(endDate.toString())) {
//                    //          传递数据到scheduleEditorView bean
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                            .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                    scheduleEditorView.setDate(selectDate);
//                    scheduleEditorView.setStudent(student);
//                    scheduleEditorView.readFromMysql();
////           程序模仿鼠标点击事件
//                    UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
//                    CommandButton cb = (CommandButton) root.findComponent("level1form:showEndDateBtn");
//                    ActionEvent actionEvent = new ActionEvent(cb);
//                    actionEvent.queue();
//                } else {
                event = new DefaultScheduleEvent("已填", selectDate, selectDate, true);
                event.setId(selectDate + "");
//          传递数据到scheduleEditorView bean
//                FacesContext context = FacesContext.getCurrentInstance();
//                web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                        .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                scheduleEditorView.setDate(selectDate);
//                scheduleEditorView.setStudent(student);
//                scheduleEditorView.readFromMysql();
//           程序模仿鼠标点击事件
                UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton cb = (CommandButton) root.findComponent("level1form:showLevel2btn1");
                ActionEvent actionEvent = new ActionEvent(cb);
                actionEvent.queue();
//                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您选择的日期还未到来，请耐心等待", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "您选择的日期不在课程内,请重新选择", ""));
        }
    }

    public void onPMDateSelect(SelectEvent selectEvent) {
        selectDate = (Date) selectEvent.getObject();
        if ((selectDate.after(startDate) || selectDate.toString().equals(startDate.toString()))
                && (selectDate.before(endDate) || selectDate.toString().equals(endDate.toString()))) {
            if (selectDate.before(today1am())) {
//                if (selectDate.toString().equals(endDate.toString())) {
//                    //          传递数据到scheduleEditorView bean
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                            .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                    scheduleEditorView.setDate(selectDate);
//                    scheduleEditorView.setStudent(student);
//                    scheduleEditorView.readFromMysql();
////           程序模仿鼠标点击事件
//                    UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
//                    CommandButton cb = (CommandButton) root.findComponent("level1form:showEndDateBtn");
//                    ActionEvent actionEvent = new ActionEvent(cb);
//                    actionEvent.queue();
//                } else {
                event = new DefaultScheduleEvent("已填", selectDate, selectDate, true);
                event.setId(selectDate + "");
//          传递数据到scheduleEditorView bean
//                FacesContext context = FacesContext.getCurrentInstance();
//                web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                        .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                scheduleEditorView.setDate(selectDate);
//                scheduleEditorView.setStudent(student);
//                scheduleEditorView.readFromMysql();
//           程序模仿鼠标点击事件
                UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton cb = (CommandButton) root.findComponent("level1form:showLevel2btn2");
                ActionEvent actionEvent = new ActionEvent(cb);
                actionEvent.queue();
//                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您选择的日期还未到来，请耐心等待", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "您选择的日期不在课程内,请重新选择", ""));
        }
    }
    
    public void onAMDateSelect2(SelectEvent selectEvent) {
        selectDate = (Date) selectEvent.getObject();
        if ((selectDate.after(startDate) || selectDate.toString().equals(startDate.toString()))
                && (selectDate.before(endDate) || selectDate.toString().equals(endDate.toString()))) {
            if (selectDate.before(today1am())) {
                  event = new DefaultScheduleEvent("已填", selectDate, selectDate, true);
                event.setId(selectDate + "");
           UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton cb = (CommandButton) root.findComponent("level1form:showLevel2btn3");
                ActionEvent actionEvent = new ActionEvent(cb);
                actionEvent.queue();
//                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您选择的日期还未到来，请耐心等待", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "您选择的日期不在课程内,请重新选择", ""));
        }
    }
    
    public void onPMDateSelect2(SelectEvent selectEvent) {
        selectDate = (Date) selectEvent.getObject();
        if ((selectDate.after(startDate) || selectDate.toString().equals(startDate.toString()))
                && (selectDate.before(endDate) || selectDate.toString().equals(endDate.toString()))) {
            if (selectDate.before(today1am())) {
//                if (selectDate.toString().equals(endDate.toString())) {
//                    //          传递数据到scheduleEditorView bean
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                            .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                    scheduleEditorView.setDate(selectDate);
//                    scheduleEditorView.setProject(project);
//                    scheduleEditorView.readFromMysql2();
////           程序模仿鼠标点击事件
//                    UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
//                    CommandButton cb = (CommandButton) root.findComponent("level1form:showEndDateBtn2");
//                    ActionEvent actionEvent = new ActionEvent(cb);
//                    actionEvent.queue();
//                } else {
                event = new DefaultScheduleEvent("已填", selectDate, selectDate, true);
                event.setId(selectDate + "");
//          传递数据到scheduleEditorView bean
//                FacesContext context = FacesContext.getCurrentInstance();
//                web.ScheduleEditorView scheduleEditorView = (web.ScheduleEditorView) context.getApplication()
//                        .getVariableResolver().resolveVariable(context, "scheduleEditorView");
//                scheduleEditorView.setDate(selectDate);
//                scheduleEditorView.setProject(project);
//                scheduleEditorView.readFromMysql2();
//           程序模仿鼠标点击事件
                UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton cb = (CommandButton) root.findComponent("level1form:showLevel2btn4");
                ActionEvent actionEvent = new ActionEvent(cb);
                actionEvent.queue();
//                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "您选择的日期还未到来，请耐心等待", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "您选择的日期不在课程内,请重新选择", ""));
        }
    }

    public Date today1am() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 1, 0, 0);
        return calendar.getTime();
    }

    public Date today0am() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return calendar.getTime();
    }

    public void showAMDig() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("schedule_editor", options, null);
    }

    public void showPMDig() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800); 
        options.put("resizable", false);
        System.out.println("scheduleView.showPMDig is called");
        RequestContext.getCurrentInstance().openDialog("pmschedule_editor", options, null);
    }

    public void showAMDig2() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        System.out.println("scheduleView.showAMDig2 is called");
        RequestContext.getCurrentInstance().openDialog("schedule_project_editor", options, null);
    }
    
    public void showPMDig2() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        System.out.println("scheduleView.showPMDig2 is called");
        RequestContext.getCurrentInstance().openDialog("pmschedule_project_editor", options, null);
    }
    
      public void showAMDig3() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("schedule_course_editor", options, null);
    }

    public void showPMDig3() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 450);
        options.put("contentWidth", 800); 
        options.put("resizable", false);
        System.out.println("scheduleView.showPMDig is called");
        RequestContext.getCurrentInstance().openDialog("pmschedule_course_editor", options, null);
    }

      public void showAMDialogEndDate() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("schedule_editor_endDate", options, null);
    }
    
    public void showPMDialogEndDate() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("pmschedule_editor_endDate", options, null);
    }

    public void showAMDialogEndDate2() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("schedule_project_editor_endDate", options, null);
    }
    
    public void showPMDialogEndDate2() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("pmschedule_project_editor_endDate", options, null);
    }
    
    public void showAMDialogEndDate3() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("schedule_course_editor_endDate", options, null);
    }
    
    public void showPMDialogEndDate3() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("height", 600);
        options.put("contentWidth", 800);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("pmschedule_course_editor_endDate", options, null);
    }

    public void onCourseReturn(SelectEvent selectevent) {
        String msg = selectevent.getObject().toString();
        System.out.println(msg);
//        if ("add".equals(msg)) {
////            eventModel.addEvent(event);
////            System.out.println(event.getTitle() + ",   " + event.getId());
//        } else if ("don't add".equals(msg)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(selectDate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "您已经成功更改" + time + "的每日进度表", ""));
//        }
        this.newCourseSchedule();
    }
    
    public void onStudentReturn(SelectEvent selectevent) {
        String msg = selectevent.getObject().toString();
        System.out.println(msg);
//        if ("add".equals(msg)) {
////            eventModel.addEvent(event);
////            System.out.println(event.getTitle() + ",   " + event.getId());
//        } else if ("don't add".equals(msg)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(selectDate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "您已经成功更改" + time + "的每日进度表", ""));
//        }
        this.newStudentSchedule();
    }

    public void onProjectReturn(SelectEvent selectevent) {
        String msg = selectevent.getObject().toString();
        System.out.println(msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(selectDate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "您已经成功更改" + time + "的每日进度表", ""));
        this.newProjectSchedule();
    }

     public void newCourseSchedule() {
        //course= null;
        //获取classplan(起止时间)需要classplanid
       // String courseId=student.getStudentPK().getCourseID();
       String courseId=course.getId();
        String classplanId=courseFacade.find(courseId).getClassplanID().getId();
        classPlan = classPlanFacade.find(classplanId);
        startDate = classPlan.getStartDate();
        endDate = classPlan.getEndDate();
        eventModel.clear();
        pmEventModel.clear();
        eventModel.addEvent(new DefaultScheduleEvent("课程时间", startDate, endDate, true));
      //  List<StudentSchedule> ssList = studentScheduleFacade.findByStudentID(student.getStudentPK().getId());
      List<CourseSchedule> csList=courseScheduleFacade.findByCID(courseId);
        for (CourseSchedule ss : csList) {
            eventModel.addEvent(new DefaultScheduleEvent(ss.getAMContent(), ss.getCourseSchedulePK().getDate(), ss.getCourseSchedulePK().getDate(), true));
            pmEventModel.addEvent(new DefaultScheduleEvent(ss.getPMContent(), ss.getCourseSchedulePK().getDate(), ss.getCourseSchedulePK().getDate(), true));
        }
        System.out.println("newCourseSchedule():" + startDate + ",   " + endDate + ",   " + student);
    }
    
    public void newStudentSchedule() {
        project = null;
        //获取classplan(起止时间)需要classplanid
        String courseId=student.getStudentPK().getCourseID();
        String classplanId=courseFacade.find(courseId).getClassplanID().getId();
        classPlan = classPlanFacade.find(classplanId);
        startDate = classPlan.getStartDate();
        endDate = classPlan.getEndDate();
        eventModel.clear();
        pmEventModel.clear();
        eventModel.addEvent(new DefaultScheduleEvent("课程时间", startDate, endDate, true));
        List<StudentSchedule> ssList = studentScheduleFacade.findByStudentID(student.getStudentPK().getId());
        for (StudentSchedule ss : ssList) {
            eventModel.addEvent(new DefaultScheduleEvent(ss.getAMContent(), ss.getStudentSchedulePK().getDate(), ss.getStudentSchedulePK().getDate(), true));
            pmEventModel.addEvent(new DefaultScheduleEvent(ss.getPMContent(), ss.getStudentSchedulePK().getDate(), ss.getStudentSchedulePK().getDate(), true));
        }
        System.out.println("newStudentSchedule():" + startDate + ",   " + endDate + ",   " + student);
    }

    public void newProjectSchedule() {
        try {
            System.out.println("scheduleView.newProjectSchedule is called");
            student = null;
            String classplanId=courseFacade.find(project.getCourseID().getId()).getClassplanID().getId();
            classPlan = classPlanFacade.find(classplanId);
            startDate = classPlan.getStartDate();
            endDate = classPlan.getEndDate();
            eventModel.clear();
            pmEventModel.clear();
            eventModel.addEvent(new DefaultScheduleEvent("课程时间", startDate, endDate, true));
            List<ProjectSchedule> psList = projectScheduleFacade.findByProjectID(project.getId());
            for (ProjectSchedule ps : psList) {
                eventModel.addEvent(new DefaultScheduleEvent(ps.getAMContent(), ps.getProjectSchedulePK().getDate(), ps.getProjectSchedulePK().getDate(), true));
                pmEventModel.addEvent(new DefaultScheduleEvent(ps.getPMContent(), ps.getProjectSchedulePK().getDate(), ps.getProjectSchedulePK().getDate(), true));
            }
            RequestContext.getCurrentInstance().execute("PF('scheduleDialog2').show()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "尚未参与项目", ""));
            RequestContext.getCurrentInstance().update("form:growl");
        }
        System.out.println("newProjectSchedule()");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Date getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(Date selectDate) {
        this.selectDate = selectDate;
    }

    public ClassPlan getClassPlan() {
        return classPlan;
    }

    public void setClassPlan(ClassPlan classPlan) {
        this.classPlan = classPlan;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public DefaultScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;
    }

    public Project getProject() {
        //project=projectFacade.find(projectId);
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the pmEventModel
     */
    public ScheduleModel getPmEventModel() {
        return pmEventModel;
    }

    /**
     * @param pmEventModel the pmEventModel to set
     */
    public void setPmEventModel(ScheduleModel pmEventModel) {
        this.pmEventModel = pmEventModel;
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
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

}
