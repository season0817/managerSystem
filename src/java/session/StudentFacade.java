/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Project;
import entity.Student;
import entity.StudentPK;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dawson
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> {

    @PersistenceContext(unitName = "manager_systemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }
    @EJB
    ProjectFacade projectFacade;
    @EJB
    StudentFacade studentFacade;
    @EJB
    UserstudentFacade userstudentFacade;

    public Student findStudent(String studentId, String projectId) {
        Query q = em.createNamedQuery("Student.findByIdPId");
        q.setParameter("id", studentId);
        q.setParameter("projectID", projectId);
        System.out.println(studentId);
        System.out.println(projectId);
        return (Student) q.getSingleResult();
    }

    public List<Student> findStudents(String userStudentId) {
        Query q = em.createNamedQuery("Student.findById");
        q.setParameter("id", userStudentId);
        return q.getResultList();
    }

    public List<Student> findStudentsByPID(String projectId) {
        Query q = em.createNamedQuery("Student.findByProjectID");
        q.setParameter("projectID", projectId);
        return q.getResultList();
    }

    public Student findStudentsByIDCID(String studentId, String courseId) {
        Query q = em.createNamedQuery("Student.findByIdCId");
        q.setParameter("courseID", courseId);
        q.setParameter("id", studentId);
        try {
            return (Student) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public String isLogin(String studentId, String studentName) {
        String flag = "falure";
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.id= :sid AND s.name= :sname");
        q.setParameter("sid", studentId);
        q.setParameter("sname", studentName);
        try {
            Student s = (Student) q.getSingleResult();
            if (studentId.equals(s.getStudentPK().getId())
                    && studentName.equals(s.getName())) {
                return "success";
            } else {
                return "failure";
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String isExist(String studentId, String projectId, String courseId) {
        String flag = "falure";
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.studentPK.id = :id and s.studentPK.projectID = :projectID");
        q.setParameter("id", studentId);
        q.setParameter("projectID", projectId);
        // q.setParameter("courseID", courseId);
        try {
            List<Student> students = q.getResultList();
            for (Student s : students) {
                if (studentId.equals(s.getStudentPK().getId())
                        && projectId.equals(s.getStudentPK().getProjectID()) && courseId.equals(s.getStudentPK().getCourseID())) {
                    return "success";
                } else {
                    return "failure";
                }
            }
        } catch (Exception e) {
            return null;
        }
        return "failure";
    }

    public void studentReportUploaded(String studentId) {
        Student s = this.find(studentId);
        if (s != null) {
            System.out.println("student查找成功");
            em.flush();
            s.setReport("1");
            em.merge(s);
        }

    }

//    @张文珍
    public void addProjectID(String projectId, String studentId, String courseId) {
        em.flush();
        // 学生参与项目函数
        StudentPK studentPK = new StudentPK(studentId, projectId, courseId);
        String name = userstudentFacade.find(studentId).getName();
        Student s = new Student(studentPK, name);
        em.persist(s);
    }

    public void delProjectID(Student s) {
        em.flush();
        //s.setProjectID(null);
        //??
        s.setStudentPK(null);
        em.merge(s);
    }

//    @朱欣悦
    public List<Student> todo(String student_id) {

        Query q = em.createQuery("SELECT s FROM Student s WHERE s.id=:student_id");
        q.setParameter("student_id", student_id);
        Student sp = (Student) q.getSingleResult();
        Query qm = em.createQuery("SELECT s FROM Student s WHERE s.projectID=:projectID");
        qm.setParameter("projectID", sp.getStudentPK().getProjectID());
        List<Student> s = qm.getResultList();
        return s;
    }

//       王睿杰
    public void addStudent(String id, String name) {
        try {
            Student s = new Student();
            em.flush();
            //s.setId(id);
            //需要projectid 为某个项目 添加学生   projectid应是确定
            //s.setStudentPK(studentPK);
            s.setName(name);
            em.persist(s);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

//朱欣悦
    public void dropStudent(StudentPK studentPK) {
        try {
            //Student s = this.find(id);
            //Query q = em.createNamedQuery("Student.findById");
            //Student s = (Student) q.getSingleResult();
            Student s=studentFacade.find(studentPK);
            em.remove(s);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public boolean changeStudentView(Student s, String selectProject) {
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.studentPK.id = :id");
        q.setParameter("id", s.getStudentPK().getId());
        Student sp = (Student) q.getSingleResult();
        if (sp != null) {
            em.flush();
//                sp.setName(s.getName());
            sp.setReport(s.getReport());
            sp.setTotalMark(s.getTotalMark());
            if (selectProject == null) {
                sp.setProject(null);

            } else {
                Project p = projectFacade.find(selectProject);
                sp.setProject(p);
            }
            em.merge(sp);
            return true;
        } else {
            return false;
        }
    }

    public List<Student> lookStudents(String id) {
        Project p = projectFacade.find(id);
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.projectID= :id");
        q.setParameter("id", p);
        List<Student> res = q.getResultList();
        for (Student s : res) {
            System.out.println(s.getStudentPK().getId() + s.getName());
        }
        return q.getResultList();
    }
}
