/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import web.ManagedBean;

/**
 *
 * @author Dawson
 */
public class ForcedLoginPhaseListener implements PhaseListener {

    public ForcedLoginPhaseListener() {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(PhaseEvent event) {

    }//在每个阶段处理之前被调用

    @Override
    public void afterPhase(PhaseEvent event) {
//在每个阶段处理之后被调用
        FacesContext fc = event.getFacesContext();
        //检查是否为 login 页
        boolean onLoginPage = (-1 != fc.getViewRoot().getViewId().lastIndexOf("index")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("login_student")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("login_manager")) ? true : false;
//        System.out.println(fc.getViewRoot().getViewId());
//是否在访问Manager才可以访问的页面
        boolean onManagerPage = (-1 != fc.getViewRoot().getViewId().lastIndexOf("manager_welcome")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("manager_projectInfo")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("manager_managerInfo")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("class_plan")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("allReportsEditor")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("change_projectReport")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("change_scheduleProject")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("change_scheduleStudent")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("change_studentReport")
                || -1 != fc.getViewRoot().getViewId().lastIndexOf("change_studentsData")) ? true : false;
//确保当前页为 login 页
        boolean isLoggedIn = false;
//        是否为Manager登陆
        boolean isManagerLoggedIn = false;

        try {
            isLoggedIn = ForcedLoginPhaseListener.checkLoginState(fc);
            ManagedBean managedBean = null;
            managedBean = (ManagedBean) fc.getApplication()
                    .getVariableResolver().resolveVariable(fc, "managedBean");
            if (managedBean.getManagerId() != null) {
                isManagerLoggedIn =true;
            }
        } catch (Exception e) {
        }
//        System.out.println("onLoginPage:" + onLoginPage + ",    isLoggedIn:" + isLoggedIn);
//        如果没有登陆的一律返回登陆界面，或者如果访问ManagerPage才可以访问的页面但Mangager没登陆那也返回登陆界面
        if ((!onLoginPage && !isLoggedIn)
                || (onManagerPage && !isManagerLoggedIn)) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index");
        }
    }

    public static boolean checkLoginState(FacesContext context) throws IOException, ServletException {
        boolean isLoggedIn = false;
//        HttpSession session = ((HttpServletRequest) request).getSession(false);
        ManagedBean managedBean = null;
        managedBean = (ManagedBean) context.getApplication()
                .getVariableResolver().resolveVariable(context, "managedBean");
// 检查 session 中是否有 UserBean，若存在 isLoggedIn 参数的值设置为 true 
        if (managedBean != null) {
            if (managedBean.isLoggedIn()) {
                isLoggedIn = true;
            }
        } else {
            System.out.println("1122");
        }
        return isLoggedIn;
    }
}
