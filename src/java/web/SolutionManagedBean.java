/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

//import entity.Solution;
/*
import entity.Student;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
//import session.SolutionFacade;
import javax.faces.bean.ManagedBean;
*/
/**
 *
 * @author kale
 */
/*
@ManagedBean(name = "solutionManagedBean")
@ViewScoped
public class SolutionManagedBean implements Serializable {
    /**
     * Solutions for students to solve problems they meets in the course
     * provide the function to upload problems and solution
     */
/*
    private List<Solution> solutions = new ArrayList<>();
*/
    //@EJB
   // private SolutionFacade solutionFacade;
/*
    private List<Solution> filteredSolutions;
    private Solution chooseSolution;
    
    private String quesiton_addString;
    private String solution_addString;
    
    HttpServletRequest request;

    public String getQuesiton_addString() {
        return quesiton_addString;
    }

    public void setQuesiton_addString(String quesiton_addString) {
        this.quesiton_addString = quesiton_addString;
    }

    public String getSolution_addString() {
        return solution_addString;
    }

    public void setSolution_addString(String solution_addString) {
        this.solution_addString = solution_addString;
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
       // solutions = solutionFacade.findAll();
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<Solution> getFilteredSolutions() {
        return filteredSolutions;
    }

    public void setFilteredSolutions(List<Solution> filteredSolutions) {
        this.filteredSolutions = filteredSolutions;
    }

    public void updateData() {
        //solutions.clear();
    //    solutions = solutionFacade.findAll();
        this.filteredSolutions = solutions;
        solutions.forEach((s) -> {
            System.out.println(s.getQuestion());
        });
    }
    
    public void addSolution() {
        if (quesiton_addString.isEmpty() || quesiton_addString == null || solution_addString.isEmpty() || solution_addString == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("添加失败,不能为空", ""));
        } else {
                FacesMessage msg = new FacesMessage("添加成功", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Solution s = new Solution();
                s.setQuestion(quesiton_addString);
                s.setSolution(solution_addString);
             //   solutionFacade.create(s);
                updateData();
        }
        quesiton_addString = null;
        solution_addString = null;
    }
    
    
    public void removeSolution(Solution s) {
        System.out.println("web.SolutionManagedBean.removeSolution()");
        Solution solution = new Solution();
                solution.setQuestion(s.getQuestion());
                solution.setSolution(s.getQuestion());
            //    solutionFacade.remove(solution);
                updateData();
    }
   
}
*/
