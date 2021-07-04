package com.timesheet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "TBL_Fruit")
public class EmployeeReports {

    @Id
    private String id;
    private Integer employeeId;
    private String employeeName;
    private String month;
    private Integer totalHrs;
    private Set<ProjectDetails> projectList;

}
