package com.timesheet.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetails {

    private Integer projectId;
    private String projectName;
    private String category;
    private String billable;
    private Integer overAllHrs;
}
