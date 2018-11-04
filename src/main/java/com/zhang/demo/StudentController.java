package com.zhang.demo;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

/**
 * Created by mrzhang on 2018/11/2.
 */
@RestController
public class StudentController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getStudent")
    @ResponseBody
    public JsonResult findStudentByAgeAndGrade(){
        StoredProcedureQuery store = this.entityManager.createNamedStoredProcedureQuery("GetStudent");
        store.setParameter("ageMin", 10);
        store.setParameter("gradeMin", 10);
        List<Student> students =  store.getResultList();
        return JsonResult.success(students);
    }

}
