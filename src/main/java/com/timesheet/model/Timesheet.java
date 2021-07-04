package com.timesheet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


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
    private String category; // Contain constant values [Remote,Onshore,Offshore,Leave]
    private String billable;
    private String taskDate;
    private String leaves;
}
