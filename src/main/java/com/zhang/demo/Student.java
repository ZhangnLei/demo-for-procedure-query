package com.zhang.demo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@NamedStoredProcedureQuery(name = "GetStudent", procedureName = "GetStudent",
    resultClasses = {Student.class},
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "ageMin", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "gradeMin", type = Integer.class)
}
)
@Entity
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
