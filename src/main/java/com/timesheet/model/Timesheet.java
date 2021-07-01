package com.timesheet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timesheet {

    @Id
    @GeneratedValue
    private Integer timesheetId;
    // User inputs from post method
    private Integer employeeId;
    private Integer projectId;
    private Integer contributionHrs;
    private String projectName;
    private String category;
    private String billable;
    private Date taskDate;
    private String leaves;
}
