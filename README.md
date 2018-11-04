# demo-for-procedure-query
Spring Data Jpa调用存储过程返回结果集
    
    使用方法：
    
    首先在数据库中创建数据库：demo-for-procedure-query
    
    修改application.properties中的数据库用户名与密码
    
    然后运行项目
    
    访问：http://127.0.0.1:8888/getStudent
    
    如有问题可邮件咨询：stephen-read@outlook.com

环境：
 1. IDEA
 2. JDK8
 3. Spring Boot
 4. MySQL

### 在数据库中创建一个存储过程
```
CREATE PROCEDURE GetStudent(IN ageMin int, IN gradeMin int)
BEGIN
	SELECT * FROM student
	WHERE age > ageMin
	AND grade > gradeMin;
END;
```

简单解释一下：第一行中的GetStudent是过程名，括号中的是参数，IN代表传入参数，OUT是传出参数, ageMin是参数名，int是参数类型（参数类型需是数据库中的参数类型）。
BEGIN和END中间的就是SQL语句

在数据库中调用存储结构的方法：
```
call GetStudent(1,2);
```

### 使用spring data jpa调用这个存储过程：
步骤： 
 1. 创建一个实体类 
 2. 在实体类中使用注解的方式绑定数据库中的存储过程 
 3. 调用这个存储过程
 
先上代码
#### 实体类：
```
@Entity
@NamedStoredProcedureQuery(name = "GetStudent", procedureName = "GetStudent",
    resultClasses = {Student.class},
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "ageMin", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "gradeMin", type = Integer.class)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    private String no;
    private String name;
    private Integer grade;
    private Integer age;
}
```
    说明：
    NamedStoredProcedureQuery 申明一个存储过程
        name属性是给这个存储结构起一个名字
        procedureName属性是存储结构在数据库中的名字
        resultClasses属性声明这个存储过程返回的结果集的类型
        parameters属性声明这个存储结构的参数
        （如果存储过程返回的是一个临时表，那么resultClasses对应的临时表的字段要与数据库中的字段匹配，匹配不意味着完全相等而是：下划线->驼峰，因为它使用的是jpa的规范）
    @Entity注解的作用是声明这是一个实体类
    下面三个注解是插件lombok的方法
    @Data 生成getter and setter方法
    @NoArgsConstructor 生成无参的构造函数
    @AllArgsConstructor 生成全参的构造函数
    使用lombok插件的方法很简单:
    先添加依赖（使用Maven构建项目）
   ```
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </dependency>
   ```
    然后在IDEA的 setting -> Plugins 搜索lombok点击安装
    最后重启IDEA
    @Id与@GeneratedValue是JPA的注解，意思是声明id字段是主键，且设置自增

#### 调用：
```
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
```
因为是demo所以就粗糙一点没有使用service层，直接在Controller中调用
```
    @PersistenceContext
    private EntityManager entityManager;
```
    
    获取spring容器中的EntityManager
    使用他的createNamedStoredProcedureQuery()方法得到一个查询对象
    使用setParameter来设置参数的值
    最后使用getResultList()方法来获取结果集

完整的代码：https://github.com/ZhangnLei/demo-for-procedure-query.git