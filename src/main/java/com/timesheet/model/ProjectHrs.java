package com.timesheet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHrs {

    // Returning the Employees Mapped Projects Hrs Details

    private Integer projectId;
    private Integer employeeId;
    private Integer contributionHrs;
    private String projectName;
    private String taskDate;
}
