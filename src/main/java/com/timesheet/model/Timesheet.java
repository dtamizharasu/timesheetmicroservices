package com.timesheet.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
@Data
public class Timesheet {

    @Id
    private Integer id;
    // User inputs from post method
    private Integer employeeId;
    private Integer projectId;
    private Integer contributionHrs;
    private String projectName;
    private String category;
    private String billable;
    private Date taskDate;
    private String leave;
}
